from abc import ABC, abstractmethod

class constraint(ABC):
    def __init__(self,variables):
        self.variables = variables
    @abstractmethod
    def filter(self):
        pass
