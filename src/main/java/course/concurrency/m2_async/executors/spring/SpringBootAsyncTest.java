package course.concurrency.m2_async.executors.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringBootAsyncTest {

    @Autowired
    private AsyncClassTest testClass;

    // this method executes after application start
    @EventListener(ApplicationReadyEvent.class)
    public void actionAfterStartup() {
        testClass.runAsyncTask();
    }

    public static void main(String[] args) {
//        SpringApplication.run(SpringBootAsyncTest.class, args);

        Stream<String> stringStream = Stream.of("a", "b", "c");
        String[] stringArray = stringStream.toArray(size -> new String[size]);
        Arrays.stream(stringArray).forEach(System.out::println);
    }
}
