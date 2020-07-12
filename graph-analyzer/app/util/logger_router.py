import sys
from logging.handlers import *
# from logging.config import stopListening
from queue import Queue

from .singleton import Singleton


class LoggerRouter(metaclass=Singleton):
    def __init__(self, listener_no=8):
        self.level = None
        self.formatter = None
        self.handlers = []
        self.initialize_default_config()
        self.queue = Queue(max(100, listener_no))
        self.queue_handler = QueueHandler(self.queue)
        self.listeners = [QueueListener(self.queue, *self.handlers) for i in range(listener_no)]
        for ls in self.listeners:
            ls.start()

    def get_logger(self, name=None):
        return self._config_logger(logging.getLogger(name))

    def _config_handler(self, hd, fmt=None, lvl=None):
        fmt = fmt if fmt else self.formatter
        lvl = lvl if lvl else self.level
        hd.setLevel(lvl)
        hd.setFormatter(fmt)
        return hd

    def _config_logger(self, logger):
        if self.queue_handler not in logger.handlers:
            logger.addHandler(self.queue_handler)
        logger.setLevel(self.level)
        return logger

    def initialize_default_config(self):
        if sys.gettrace() is not None or os.getenv('DEBUG', False):
            self.level = logging.DEBUG
        else:
            self.level = logging.INFO
        self.formatter = logging.Formatter('%(asctime)s %(levelname)-8s [%(name)s:%(lineno)s] %(message)s',
                                           datefmt='%Y-%m-%d %H:%M:%S')
        self.handlers = [
            # logging.NullHandler(),
            self._config_handler(
                logging.StreamHandler(sys.stdout),
            )
        ]

    def stop(self):
        for l in self.listeners:
            l.enqueue_sentinel()
        for l in self.listeners:
            l._thread.join()
            l._thread = None
