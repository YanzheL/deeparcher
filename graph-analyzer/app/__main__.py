from __future__ import annotations

from typing import *

if TYPE_CHECKING:
    from app.analyzer import GraphAnalyzer

    pass

from app.analyzer import ConnectedComponentsAnalyzer, NodeAttrMatcherAnalyzer, BeliefPropagationAnalyzer
from app.util.misc import load_lines_as_array
from tld import get_fld
from functools import partial

if __name__ == '__main__':
    path = 'data/graph.dot.pickle'
    # g = from_dot(path,extract_node_attrs=True)
    import pickle

    with open(path, 'rb') as f:
        initial_graph = pickle.load(f)

    context = {}

    graphs = ConnectedComponentsAnalyzer().analyze(initial_graph, context, cc_size_thres=100)
    analyzers: List[Tuple[GraphAnalyzer, dict]] = []
    analyzers.append((
        NodeAttrMatcherAnalyzer(
            load_lines_as_array('data/blacklist.txt'),
            load_lines_as_array('data/whitelist.txt')
        ),
        {
            'src_attr': 'label',
            'dst_attr': 'black_or_white',
            'value_false_mapper': partial(get_fld, fail_silently=True, fix_protocol=True)
        }
    )
    )
    analyzers.append((BeliefPropagationAnalyzer(), {}))
    # analyzers.append((LLGCAnalyzer(), {}))
    # analyzers.append((TopologyBasedFlowAnalyzer(), {}))
    # analyzers.append((PathBasedInferenceAnalyzer(), {}))

    for graph in graphs:
        for analyzer, runtime_configs in analyzers:
            graph = analyzer.analyze(graph, context, **runtime_configs)
