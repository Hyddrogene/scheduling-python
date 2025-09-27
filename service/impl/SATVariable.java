package cb_ctt.features.service.impl;

import cb_ctt.dto.Course;
import cb_ctt.dto.Curriculum;
import cb_ctt.dto.Room;

public class SATVariable {

    private Course course;
    private Curriculum curriculum;
    private int day;
    private int slot;
    private Room room;
    private String type;
    private boolean negated;
    private int id;

    SATVariable(int id, String type, Course course, int slot, int day, Room room, Curriculum curriculum, boolean negated) {
        this.id = id;
        this.type = type;
        this.course = course;
        this.slot = slot;
        this.day = day;
        this.room = room;
        this.negated = negated;
        this.curriculum = curriculum;
    }
}
