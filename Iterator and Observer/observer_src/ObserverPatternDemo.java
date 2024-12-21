import java.util.ArrayList;
import java.util.List;

interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();

    int getState();
    void setState(int state);
}

interface Observer {
    void subscribe(Subject subscriber);
    void update(int state);

    Subject getSubject();
}

class ConcreteSubject implements Subject {
    private List<Observer> observers;
    private int state;

    public ConcreteSubject() {
        observers = new ArrayList<>();
    }

    public Observer createObserver() {
        var result = new ConcreteObserver(this);
        registerObserver(result);
        return result;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }

    @Override
    public void setState(int state) {
        this.state = state;
        notifyObservers();
    }

    @Override
    public int getState() {
        return this.state;
    }
}

class ConcreteObserver implements Observer {
    private Subject subject;
    private List<Subject> subscribers;


    public ConcreteObserver(Subject subject) {
        this.subject = subject;
        this.subject.registerObserver(this);
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void update(int state) {
        for (Subject subject : subscribers) {
            if (subject.getState() != state) {
                subject.setState(state);
            }
        }
    }

    @Override
    public Subject getSubject() {
        return this.subject;
    }

    @Override
    public void subscribe(Subject subscriber) {
        subscribers.add(subscriber);
    }
}

public class ObserverPatternDemo {
    public static void firstExample() {
        ConcreteSubject mainSubject = new ConcreteSubject();

        ConcreteSubject subject1 = new ConcreteSubject();
        ConcreteSubject subject2 = new ConcreteSubject();
        ConcreteSubject subject3 = new ConcreteSubject();

        subject1.setState(1);
        subject2.setState(2);
        subject3.setState(3);

        System.out.println("subject1 state: " + subject1.getState());
        System.out.println("subject2 state: " + subject2.getState());
        System.out.println("subject3 state: " + subject3.getState());

        Observer mainObserver = mainSubject.createObserver();

        mainObserver.subscribe(subject1);
        mainObserver.subscribe(subject2);
        mainObserver.subscribe(subject3);

        mainSubject.setState(42);

        System.out.println("subject1 state: " + subject1.getState());
        System.out.println("subject2 state: " + subject2.getState());
        System.out.println("subject3 state: " + subject3.getState());
    }

    public static void secondExample() {
        ConcreteSubject subject1 = new ConcreteSubject();
        subject1.setState(1);
        Observer Observer1 = new ConcreteObserver(subject1);

        ConcreteSubject subject2 = new ConcreteSubject();
        subject2.setState(2);
        Observer Observer2 = new ConcreteObserver(subject2);

        Observer1.subscribe(subject2);
        Observer2.subscribe(subject1);

        System.out.println("subject1 state: " + subject1.getState());
        System.out.println("subject2 state: " + subject2.getState());

        subject1.setState(42);
        
        System.out.println("subject1 state: " + subject1.getState());
        System.out.println("subject2 state: " + subject2.getState());
        
        subject2.setState(24);

        System.out.println("subject1 state: " + subject1.getState());
        System.out.println("subject2 state: " + subject2.getState());
    }

    public static void main(String[] args) {
        firstExample();

        secondExample();
    }
}
