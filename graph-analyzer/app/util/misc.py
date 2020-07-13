from __future__ import annotations

import time
from typing import Dict, Tuple

import numpy as np
from tld import get_fld

from app.util.logger_router import LoggerRouter

logger = LoggerRouter().get_logger(__name__)


def normalize(a):
    if a.any():
        max = a.max()
        min = a.min()
        diff = max - min
        return (a - min) if diff == 0 else (a - min) / diff
    else:
        return a


def normalize_sparse(m):
    coo = m.tocoo()
    coo.data = normalize(coo.data)
    return coo.tocsr()


def load_lines_as_array(path: str) -> np.ndarray:
    logger.info('Loading file <{}>...'.format(path))
    with open(path, 'r') as f:
        lines = np.array([i.strip() for i in f.read().splitlines()], dtype=str)
        logger.info('Loaded {} items from file <{}>'.format(lines.size, path))
        return lines


def load_blacklist(path: str) -> np.ndarray:
    logger.info('Loading blacklist file <{}>...'.format(path))
    with open(path, 'r') as f:
        blacklist = f.read().splitlines()
        blacklist = np.unique([i.lower().strip() for i in blacklist])
        logger.info('Loaded {} unique FQDNs from blacklist file <{}>'.format(blacklist.size, path))
        return blacklist


def load_whitelist(path: str) -> np.ndarray:
    logger.info('Loading whitelist file <{}>...'.format(path))
    with open(path, 'r') as f:
        whitelist = f.read().splitlines()
        whitelist = list(filter(
            lambda x: x is not None,
            map(lambda i: get_fld(i, fail_silently=True, fix_protocol=True), whitelist)
        ))
        whitelist = np.unique(whitelist)
        logger.info('Loaded {} unique FLDs from whitelist file <{}>'.format(whitelist.size, path))
        return whitelist


def timing(method):
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


def extract_bool_attr_ids(name: str, attrs: Dict[str, Dict[int, object]]) -> Tuple[np.ndarray, np.ndarray]:
    true_nodes = []
    false_nodes = []
    attr = attrs.get(name, {})
    for k, v in attr.items():
        target = true_nodes if v else false_nodes
        target.append(k)
    return np.array(true_nodes, dtype=np.int32), np.array(false_nodes, dtype=np.int32)
