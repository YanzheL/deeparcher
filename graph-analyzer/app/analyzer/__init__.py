from app.util.logger_router import LoggerRouter
from .interface import GraphAnalyzer

DEFAULT_LOGGER = LoggerRouter().get_logger(__name__)
try:
    from .belief_propagation_analyzer import BeliefPropagationAnalyzer
except ImportError as e:
    DEFAULT_LOGGER.warn(e)
    BeliefPropagationAnalyzer = None
try:
    from .connected_component_analyzer import ConnectedComponentsAnalyzer
except ImportError as e:
    DEFAULT_LOGGER.warn(e)
    ConnectedComponentsAnalyzer = None
try:
    from .llgc_analyzer import LLGCAnalyzer
except ImportError as e:
    DEFAULT_LOGGER.warn(e)
    LLGCAnalyzer = None
try:
    from .node_attr_matcher_analyzer import NodeAttrMatcherAnalyzer
except ImportError as e:
    DEFAULT_LOGGER.warn(e)
    NodeAttrMatcherAnalyzer = None
try:
    from .path_based_inference_analyzer import PathBasedInferenceAnalyzer
except ImportError as e:
    DEFAULT_LOGGER.warn(e)
    PathBasedInferenceAnalyzer = None
try:
    from .topology_based_flow_analyzer import TopologyBasedFlowAnalyzer
except ImportError as e:
    DEFAULT_LOGGER.warn(e)
    TopologyBasedFlowAnalyzer = None
