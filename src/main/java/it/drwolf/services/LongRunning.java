package it.drwolf.services;

import io.quarkus.vertx.ConsumeEvent;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class LongRunning {
	public final static String LONG = "LONG";

	private final PublishSubject<String> subject = PublishSubject.create();

	private void doWork(String data){
		System.out.println("waiting...");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println(data);
	}

	public LongRunning() {
		this.subject.debounce(10, TimeUnit.SECONDS).subscribe(this::doWork);
	}

	@ConsumeEvent(LONG)
	public void longJob(String data){
		this.subject.onNext(data);
	}

}
