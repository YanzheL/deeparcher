from typing import TYPE_CHECKING

if TYPE_CHECKING:
    import cugraph
    from scipy.sparse import spmatrix


def to_cugraph(adj: spmatrix, symmetrized=True, weight_transform=None) -> cugraph.Graph:
    import cudf
    import cugraph
    adj = adj.tocoo()
    df = cudf.DataFrame()
    df['src'] = adj.row
    df['dst'] = adj.col
    df['weights'] = adj.data
    if weight_transform is not None:
        df['weights'] = df['weights'].applymap(weight_transform)
    g = cugraph.Graph(symmetrized=symmetrized)
    g.from_cudf_edgelist(df, 'src', 'dst', 'weights')
    # adj = self.graph.adj.tocsr()
    # offsets = cudf.Series(adj.indptr)
    # indices = cudf.Series(adj.indices)
    # g.from_cudf_adjlist(offsets, indices, None)
    return g
