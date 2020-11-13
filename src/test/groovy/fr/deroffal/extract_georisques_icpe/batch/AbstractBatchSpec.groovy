package fr.deroffal.extract_georisques_icpe.batch

import fr.deroffal.extract_georisques_icpe.service.DateService
import fr.deroffal.extract_georisques_icpe.util.DbSpec
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.batch.core.BatchStatus.COMPLETED
import static org.springframework.batch.core.ExitStatus.UNKNOWN
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class AbstractBatchSpec extends Specification implements DbSpec {

    private static final int MAX_RETRY_ATTEMPT = 10
    private static final int SLEEP_STEP_MILLIS = 500

    @Value('${local.server.port}')
    int localServerPort

    @Autowired
    JobExplorer jobExplorer

    @Autowired
    DateService dateService

    void setup(){
        injectData()
    }

    void cleanup() {
        verifyDb()
    }

    protected void waitForJobExecutionToFinish(final Long jobExecutionId = 1L) {
        Integer counter = 0
        JobExecution jobExe = jobExplorer.getJobExecution(jobExecutionId)
        while (isUnderTimeout(counter) && isJobStillRunning(jobExe)) {
            Thread.sleep(SLEEP_STEP_MILLIS)
            counter++
            jobExe = jobExplorer.getJobExecution(jobExecutionId)
        }
        assert jobExe, "Aucun job lancé avec l'id $jobExecutionId, vérifiez que l'execution de ce job a bien été lancée"
    }

    private static boolean isUnderTimeout(int counter) {
        counter < MAX_RETRY_ATTEMPT
    }

    private static boolean isJobStillRunning(JobExecution jobExe) {
        jobExe?.isRunning() || jobExe?.exitStatus in [UNKNOWN]
    }

    protected boolean verifyJobStatusFinished(final Long jobExecutionId = 1L) {
        jobExplorer.getJobExecution(jobExecutionId).status == COMPLETED
    }

    protected String getBaseUrl() {
        return "http://localhost:${localServerPort}"
    }
}
