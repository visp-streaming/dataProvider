package ac.at.tuwien.infosys.visp.dataProvider.entities;

import java.util.HashMap;
import java.util.Map;

public class GenerationState {

    private long id;

    private String direction;
    private Integer iteration;
    private Integer amount;
    private String time;
    private Integer actualAmount;
    private Integer overallCounter;
    private Map<String, Object> additionalProperties;
    private int customData[];

    public GenerationState() {
        direction = "up";
        iteration = 1;
        amount = 1;
        overallCounter = 0;
        additionalProperties = new HashMap<>();
    }

    public void increaseIteration() {
        this.iteration += 1;
    }

    public void increaseAmount() {
        this.amount += 1;
    }

    public void decreaseAmount() {
        this.amount -= 1;
    }

    public void increaseOverallCounter() {
        this.overallCounter += 1;
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

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Integer getAdditionalProperty(String name){
        return (Integer)additionalProperties.getOrDefault(name, 1);
    }

    public int[] getCustomData() {
        return customData;
    }

    public void setCustomData(int[] customData) {
        this.customData = customData;
    }
}
