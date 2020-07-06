import time
from typing import List, Tuple

import numpy as np
from tld import get_fld

from app.struct.graph_attributes import GraphElementsAttrMap
from app.util.logger_router import LoggerRouter

default_logger = LoggerRouter().get_logger(__name__)


def load_blacklist(path: str, logger=default_logger) -> np.ndarray:
    logger.info('Loading blacklist file <{}>...'.format(path))
    with open(path, 'r') as f:
        blacklist = f.read().splitlines()
        blacklist = set(i.lower().strip() for i in blacklist)
        blacklist = np.array(blacklist, dtype=str)
        logger.info('Loaded {} unique FQDNs from blacklist file <{}>'.format(blacklist.size, path))
        return blacklist


def load_whitelist(path: str, logger=default_logger) -> np.ndarray:
    logger.info('Loading whitelist file <{}>...'.format(path))
    with open(path, 'r') as f:
        whitelist = f.read().splitlines()
        whitelist = set(filter(
            lambda x: x is not None,
            map(lambda i: get_fld(i, fail_silently=True, fix_protocol=True), whitelist)
        ))
        whitelist = np.array(whitelist, dtype=str)
        logger.info('Loaded {} unique FQDNs from whitelist file <{}>'.format(whitelist.size, path))
        return whitelist


def timing(method, logger=default_logger):
    def timed(*args, **kw):
        ts = time.perf_counter()
        result = method(*args, **kw)
        te = time.perf_counter()
        if 'log_time' in kw:
            name = kw.get('log_name', method.__name__.upper())
            kw['log_time'][name] = (te - ts) * 1000
        else:
            logger.info('%r  %2.3f ms' % (method.__name__, (te - ts) * 1000))
        return result

    return timed


def extract_labels(name: str, attrs: List[GraphElementsAttrMap], n_expected=-1) -> np.ndarray:
    node_labels = []
    for attr in attrs:
        if attr.name == name:
            items: List[Tuple[int, object]] = attr.data.items()
            items.sort(key=lambda x: x[0])
            for _, data in items:
                node_labels.append(data)
            break
    if 0 < n_expected != len(node_labels):
        raise ValueError(
            "Expected {} attributes of {}, found {} instead".format(n_expected, name, len(node_labels)))
    return np.array(node_labels, dtype=str)
