package org.teamapps.demolessons.p2_application.l01_backgroundtasks;

import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.progress.ProgressDisplay;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.ux.task.ObservableProgress;
import org.teamapps.ux.task.ProgressCompletableFuture;
import org.teamapps.ux.task.ProgressMonitor;

import java.util.concurrent.ThreadLocalRandom;

public class BackgroundTasksDemo implements DemoLesson {

    private SessionContext sessionContext;

    public BackgroundTasksDemo(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Override
    public Component getRootComponent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        Button<BaseTemplateRecord> button = Button.create(MaterialIcon.ALARM, "Calculate Pi");
        ProgressDisplay progressDisplay = new ProgressDisplay();
        verticalLayout.addComponent(button);
        verticalLayout.addComponent(progressDisplay);

        button.onValueChanged.addListener(aBoolean -> {

            ProgressCompletableFuture<Double> future = ProgressCompletableFuture
                    .supplyAsync(progressMonitor -> {
                        return BackgroundTasksDemo.this.calculatePi(5_000, progressMonitor);
                    });

            ObservableProgress progress = future.getProgress();
            progressDisplay.setObservedProgress(progress);
            progressDisplay.setIcon(MaterialIcon.PERSON);
            progressDisplay.setTaskName("Calculate Pi");

            future.thenAcceptWithCurrentSessionContext(pi -> {
                if (pi != null) {
                    sessionContext.showNotification(MaterialIcon.ALARM, "Pi is " + pi);
                }
            });

        });



        return verticalLayout;
    }

    private Double calculatePi(int numberOfIterations, ProgressMonitor progressMonitor) {
        progressMonitor.setCancelable(true);
        progressMonitor.setStatusMessage("Working hard for you!");
        long insideQuarterCircleCount = 0;
        for (long i = 0; i < numberOfIterations; i++) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            if (Math.sqrt(x * x + y * y) < 1) {
                insideQuarterCircleCount++;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i % (numberOfIterations / 100) == 0) {
                progressMonitor.setProgress((float) i / numberOfIterations, ((i * 100 / numberOfIterations) ) + "%");
            }
            if (progressMonitor.isCancellationRequested()) {
                progressMonitor.markCanceled();
                return null;
            }
        }
        double result = insideQuarterCircleCount * 4 / (double) numberOfIterations;
        progressMonitor.setStatusMessage("We are done!");
        return result;
    }
}
