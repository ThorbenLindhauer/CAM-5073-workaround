/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.test;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Thorben Lindhauer
 *
 */
public class Cam5073Test {

  protected static final int NUM_INSTANCES = 100;

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule("camunda.cfg.xml");

  static {
    LogFactory.useSlf4jLogging();
  }

  @Test
  @Deployment(resources = "org/camunda/bpm/test/nonExclusiveJob.bpmn20.xml")
  public void testNoBackoffOnJobAddedNotification() throws InterruptedException {
    ProcessEngine processEngine = rule.getProcessEngine();

    /*
     * Some initial load so job executor is not immediately idle
     */
    for (int i = 0; i < NUM_INSTANCES; i++) {
      processEngine.getRuntimeService().startProcessInstanceByKey("process");
    }

    ProcessEngineConfigurationImpl engineConfiguration =
        (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();

    engineConfiguration.getJobExecutor().start();

    /*
     * Some new instances that trigger job added notifications; the bug behavior can be observed
     * if the job executor is not yet sleeping while these notifications are issued;
     *
     * => in effect, no log entries reporting the sleep process are seen but there are
     *   constant acquisiton queries
     */
    for (int i = 0; i < NUM_INSTANCES; i++) {
      processEngine.getRuntimeService().startProcessInstanceByKey("process");
    }

    // timeout to wait for job executor logs
    Thread.sleep(10000L);


  }
}
