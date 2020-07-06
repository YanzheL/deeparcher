class MutableBean:
    def __init__(self):
        self.__modified_attrs = {}

    @property
    def modified_attrs(self):
        return self.__modified_attrs

    def reset(self):
        for name, value in self.__modified_attrs.items():
            priv_name = '_{}'.format(name)
            setattr(self, priv_name, value)

    def _set_property(self, name, value) -> bool:
        priv_name = '_{}'.format(name)
        old_val = getattr(self, priv_name, None)
        if old_val != value:
            setattr(self, priv_name, value)
            if name not in self.__modified_attrs:
                self.__modified_attrs[name] = old_val
            return True
        return False
