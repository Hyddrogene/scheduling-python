#include <gecode/driver.hh>
#include <gecode/int.hh>
#include <gecode/minimodel.hh>

using namespace Gecode;

class RCPSP : public Space {
protected:
    static const int num_tasks = 3;
    IntVarArray start_times;
    IntVar makespan;

public:
    RCPSP() : start_times(*this, num_tasks, 0, 10), makespan(*this, 0, 30) {
        // Durées des tâches
        int durations[] = {3, 2, 4};

        // Contraintes de précédence : tâche 0 précède tâche 1
        rel(*this, start_times[0] + durations[0] <= start_times[1]);

        // Contraintes de ressources cumulatives
        // Chaque tâche utilise 1 unité de ressource
        IntArgs heights(num_tasks, 1, 1, 1);
        IntArgs durations_args(num_tasks, durations[0], durations[1], durations[2]);
        cumulatives(*this, start_times, durations_args, heights, 2);

        // Calcul du makespan
        IntVarArgs end_times(*this, num_tasks, 0, 30);
        for (int i = 0; i < num_tasks; i++) {
            rel(*this, end_times[i] == start_times[i] + durations[i]);
        }
        max(*this, end_times, makespan);

        // Définir le problème d'optimisation
        branch(*this, start_times, INT_VAR_SIZE_MIN(), INT_VAL_MIN());
    }

    // Constructeur de copie
    RCPSP(bool share, RCPSP& s) : Space(share, s) {
        start_times.update(*this, share, s.start_times);
        makespan.update(*this, share, s.makespan);
    }

    virtual Space* copy(bool share) {
        return new RCPSP(share, *this);
    }

    void print(std::ostream& os) const {
        os << "Start times: ";
        for (int i = 0; i < num_tasks; i++) {
            os << start_times[i] << " ";
        }
        os << "\nMakespan: " << makespan << std::endl;
    }
};

int main(int argc, char* argv[]) {
    RCPSP* m = new RCPSP;
    DFS<RCPSP> e(m);
    delete m;

    RCPSP* s = e.next();
    if (s) {
        s->print(std::cout);
        delete s;
    } else {
        std::cout << "No solution found." << std::endl;
    }

    return 0;
}
