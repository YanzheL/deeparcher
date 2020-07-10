from __future__ import annotations

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from scipy.sparse import spmatrix

import cudf
import cugraph


def build_cugraph(adj: spmatrix, symmetrized=True) -> cugraph.Graph:
    adj = adj.tocoo()
    df = cudf.DataFrame()
    df['src'] = adj.row
    df['dst'] = adj.col
    df['weight'] = adj.data
    g = cugraph.Graph(symmetrized=symmetrized)
    g.from_cudf_edgelist(df, 'src', 'dst', 'weight')
    # adj = self.graph.adj.tocsr()
    # offsets = cudf.Series(adj.indptr)
    # indices = cudf.Series(adj.indices)
    # g.from_cudf_adjlist(offsets, indices, None)
    return g
