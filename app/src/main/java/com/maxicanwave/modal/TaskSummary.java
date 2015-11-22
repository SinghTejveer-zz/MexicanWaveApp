package com.maxicanwave.modal;

/**
 * Created by Tejveer on 11/13/2015.
 */
public class TaskSummary {private int id;
    private int g_id;
    private int u_id;
    private int task;
    private int task_status;
    private String groupName;

    //	0 for none
    //	1 for create notification by group owner
    //	2 for deny task by member

    public TaskSummary() {
        super();
        // TODO Auto-generated constructor stub
    }







    public TaskSummary(int id, int g_id, int u_id, int task, int task_status,String groupName) {
        super();
        this.id = id;
        this.g_id = g_id;
        this.u_id = u_id;
        this.task = task;
        this.task_status = task_status;
        this.groupName = groupName;
    }







    public int getTask_status() {
        return task_status;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setTask_status(int task_status) {
        this.task_status = task_status;
    }







    public int getTask() {
        return task;
    }





    public void setTask(int task) {
        this.task = task;
    }





    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getG_id() {
        return g_id;
    }


    public void setG_id(int g_id) {
        this.g_id = g_id;
    }


    public int getU_id() {
        return u_id;
    }


    public void setU_id(int u_id) {
        this.u_id = u_id;
    }




}
