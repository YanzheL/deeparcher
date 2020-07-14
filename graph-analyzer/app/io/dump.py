from __future__ import annotations

from typing import *

if TYPE_CHECKING:
    pass

import numpy as np


def merge_attributes(data: Dict[str, np.ndarray], id_remap: np.ndarray, out: Dict[str, np.ndarray],
                     ignored_attrs: List[str]) -> NoReturn:
    parent_sample = next(iter(out.values()))
    parent_size = parent_sample.size
    id_reverse_map = {i: idx for idx, i in enumerate(id_remap)}
    for name, values in data.items():
        if name in ignored_attrs:
            continue
        parent_values = out.get(name, np.zeros((parent_size,), dtype=values.dtype))
        for idx, value in enumerate(values):
            parent_values[id_reverse_map[idx]] = value
        out[name] = parent_values


def dump_attributes(attrs: Dict[str, Dict[int, object]], ignored_attrs: List[str]) -> Dict[str, np.ndarray]:
    data = {}
    for name, attr in attrs.items():
        if name in ignored_attrs:
            continue
        data[name] = _sparse_dict_to_dense(attr)
    return data


def _sparse_dict_to_dense(data: Dict[int, object], dtype=None) -> np.ndarray:
    size = max(data.keys()) + 1
    if dtype is None:
        sample = next(iter(data.values()))
        if isinstance(sample, str):
            dtype = str
        elif isinstance(sample, (int, np.int32)):
            dtype = np.int32
        elif isinstance(sample, (float, np.float32)):
            dtype = np.float32
    ret = np.zeros((size,), dtype=dtype)
    for k, v in data.items():
        ret[k] = v
    return ret
