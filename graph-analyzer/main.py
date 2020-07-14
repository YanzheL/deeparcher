from __future__ import annotations

from typing import *

if TYPE_CHECKING:
    from app.analyzer import GraphAnalyzer
    from app.struct.graph import Graph
    import numpy as np

from app.analyzer import NodeAttrMatcherAnalyzer
from app.util.misc import load_blacklist, load_whitelist
from tld import get_fld
from functools import partial
from importlib import import_module
from queue import Queue
import pandas as pd
from app.util.logger_router import LoggerRouter

logger = LoggerRouter().get_logger(__name__)


def load_analyzer_instances(name: str, *args, **kwargs) -> GraphAnalyzer:
    mod = import_module('app.analyzer')
    return getattr(mod, name)(*args, **kwargs)


def load_graph(type: str, path: str) -> Graph:
    import app.io as gio
    if type.lower() == 'dot':
        return gio.from_dot(path, True)
    elif type.lower() == 'pb':
        return gio.from_pb(path)


def load_inputs(graph, blacklist, whitelist) -> Tuple[Graph, np.ndarray, np.ndarray]:
    return load_graph(**graph), load_blacklist(blacklist['path'], blacklist['validate']), load_whitelist(
        whitelist['path'], whitelist['validate'])


def load_yaml(path: str) -> dict:
    import yaml
    try:
        from yaml import CLoader as Loader, CDumper as Dumper
    except ImportError:
        from yaml import Loader, Dumper
    with open(path) as f:
        data = yaml.load(f, Loader=Loader)
    return data


def dump_node_attributes(parent_nodes, graph: Graph, shared_data: Dict[str, np.ndarray],
                         ignored_attrs: List[str] = None):
    from app.io import dump_attributes, merge_attributes
    merge_attributes(dump_attributes(graph.node_attrs, ignored_attrs), graph.node_id_remap, shared_data, parent_nodes)


def main(inputs, output, analyzers):
    initial_graph, blacklist, whitelist = load_inputs(**inputs)
    logger.info("Loaded inputs")
    pipeline: List[Tuple[GraphAnalyzer, dict]] = [(load_analyzer_instances(analyzer['name']), analyzer['config'])
                                                  for analyzer in analyzers if analyzer['enable']]
    logger.info("Loaded analyzers")
    # return
    context = {}
    initial_analyzer = NodeAttrMatcherAnalyzer(
        blacklist,
        whitelist
    )
    logger.info("Loaded initial_analyzer")
    initial_graph = initial_analyzer.analyze(
        initial_graph,
        context,
        src_attr='label',
        dst_attr='black_or_white',
        value_false_mapper=partial(get_fld, fail_silently=True, fix_protocol=True)
    )
    logger.info("Analyzed initial_graph")
    graphs = Queue()
    initial_graph.meta['initial'] = True
    parent_nodes = initial_graph.nodes
    graphs.put(initial_graph)
    analyzed = Queue()
    while not graphs.empty():
        graph = graphs.get()
        for analyzer, runtime_configs in pipeline:
            ret = analyzer.analyze(graph, context, **runtime_configs)
            if isinstance(ret, list):
                for g in ret:
                    graphs.put(g)
                break
            else:
                graph = ret
        analyzed.put(graph)
    logger.info("Analysis finished")
    all_attributes: Dict[str, np.ndarray] = {}
    while not analyzed.empty():
        graph = analyzed.get()
        ignored_attrs = ['fqdn']
        if 'initial' not in graph.meta:
            ignored_attrs = None
        dump_node_attributes(parent_nodes, graph, all_attributes, ignored_attrs)
    df = pd.DataFrame(data=all_attributes)
    df.to_csv(output['path'])
    logger.info("Output finished")


if __name__ == '__main__':
    app_config = load_yaml('application.yml')['app']
    logger.debug(f"Loaded config = \n{app_config}")
    main(**app_config)
