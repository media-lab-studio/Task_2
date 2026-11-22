package burgers;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;

public class ConsoleStepLogger implements StepLifecycleListener {
    @Override
    public void beforeStepStart(StepResult step) {
        System.out.printf("[ШАГ] %s%n", step.getName());
    }

    @Override
    public void afterStepStop(StepResult step) {
        System.out.printf("[%s] %s%n", step.getStatus(), step.getName());
    }
}
