package it.drwolf.services;

import it.drwolf.repositories.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Date;

@ApplicationScoped
public class TaskScheduler {

	@Inject
	Scheduler quartz;

	@Inject
	BookRepository bookRepository;


	public void scheduleTask(){
		try {
			quartz.clear();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
		bookRepository.findAll().stream().forEach(b->{
			Trigger t = TriggerBuilder.newTrigger()
					.withSchedule(SimpleScheduleBuilder.repeatHourlyForever()).build();

			JobDetail jobDetail = JobBuilder.newJob(BookJob.class)
					.withIdentity("%d".formatted(b.id), "book-jobs").build();

			try {
				quartz.scheduleJob(jobDetail,t);
			} catch (SchedulerException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
