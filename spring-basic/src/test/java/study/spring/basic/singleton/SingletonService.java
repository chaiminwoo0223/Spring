package study.spring.basic.singleton;

public class SingletonService {
    private static SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }

    private SingletonService() {}
}
