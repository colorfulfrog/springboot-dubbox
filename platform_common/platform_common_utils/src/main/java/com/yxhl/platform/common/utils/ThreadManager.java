package com.yxhl.platform.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 *线程池管理类
 */
public class ThreadManager {
	/**
	 * 线程数
	 */
	private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
	
	/**
	 * 线程池
	 */
	private static ScheduledExecutorService SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(POOL_SIZE);
	
	/**
	 * 保存任务执行结果
	 */
	public static final ConcurrentHashMap<String, Future<?>> FUTURE_MAP = new ConcurrentHashMap<String, Future<?>>();
	
	
	/**
	 * 在指定时间运行线程
	 * @param command 要执行的线程
	 * @param date 指定时间
	 * @return
	 */
	public static Future<?> execute(Runnable command,Date date) {
		ScheduledFuture<?> future = null;
		Date now = Calendar.getInstance().getTime();
		//启动时间在当前时间之前,立即启动
		if(date.compareTo(now) <= 0) {
			future = SCHEDULED_THREAD_POOL.schedule(command, 0, TimeUnit.SECONDS);
		}else {
			long diff = DataUtil.compareTime(now, date);
			future = SCHEDULED_THREAD_POOL.schedule(command, diff, TimeUnit.SECONDS);
		}
		
		return future;
	}
	
	/**
	 * 运行一个线程
	 * @param command 要执行的线程
	 * @return
	 */
	public static Future<?> execute(Runnable command) {
		return SCHEDULED_THREAD_POOL.submit(command);
	}
	
	/**
	 * 取消正在执行的线程
	 * @param taskName 保存的任务名称
	 * @return
	 */
	public static boolean cancel(String taskName) {
		boolean isCancled = false;
		Future<?> future = FUTURE_MAP.get(taskName);
		if(future != null) {
			isCancled = future.cancel(true);
		}
		FUTURE_MAP.remove(taskName);
		return isCancled;
	}
}
