package vispDataProvider.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GenerationState {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String direction;
    private Integer iteration;
    private Integer amount;
    private String time;
    private Integer actualAmount;
    private Integer overallCounter;

    public GenerationState() {
        direction = "up";
        iteration = 1;
        amount = 1;
        overallCounter = 0;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Integer actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getOverallCounter() {
        return overallCounter;
    }

    public void setOverallCounter(Integer overallCounter) {
        this.overallCounter = overallCounter;
    }
}
