from __future__ import annotations

from typing import *

if TYPE_CHECKING:
    pass

import numpy as np


def merge_attributes(data: Dict[str, np.ndarray], id_remap: np.ndarray, out: Dict[str, np.ndarray], parent_size,
                     ignored_attrs: List[str] = None) -> NoReturn:
    for name, values in data.items():
        if ignored_attrs is not None and name in ignored_attrs:
            continue
        parent_values = out.get(name, np.zeros((parent_size,), dtype=values.dtype))
        for idx, value in enumerate(values):
            parent_values[id_remap[idx]] = value
        out[name] = parent_values


def dump_attributes(attrs: Dict[str, Dict[int, object]], ignored_attrs: List[str] = None) -> Dict[str, np.ndarray]:
    data = {}
    for name, attr in attrs.items():
        if ignored_attrs is not None and name in ignored_attrs:
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
