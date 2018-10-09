package com.yxhl.common.mq.config;

public class MessageVerbTypes {

	/**
	 * 动作枚举。
	 * 必须和\ELEAD_WEB_UI\02_Src\eppm\static\js\template-filter.js中的verbs中的值对应
	 * @author liu.tao
	 *
	 */
	public enum Verbs{
		discuss_add,
		discuss_delete,
		discuss_edit,
		task_add,
		task_delete,
		task_edit,
		agenda_add,
		agenda_delete,
		agenda_edit,
		document_add,
		document_delete,
		document_edit,
		document_file_add,
		document_file_delete,
		document_file_edit
	}
}
