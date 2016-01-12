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
package org.camunda.bpm.workaround;

import org.camunda.bpm.engine.impl.jobexecutor.JobAcquisitionContext;
import org.camunda.bpm.engine.impl.jobexecutor.JobAcquisitionStrategy;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.impl.jobexecutor.SequentialJobAcquisitionRunnable;

/**
 * @author Thorben Lindhauer
 *
 */
public class FixedSequentialJobAcquisitionRunnable extends SequentialJobAcquisitionRunnable {

  public FixedSequentialJobAcquisitionRunnable(JobExecutor jobExecutor) {
    super(jobExecutor);
  }

  @Override
  protected void configureNextAcquisitionCycle(JobAcquisitionContext acquisitionContext, JobAcquisitionStrategy acquisitionStrategy) {
    super.configureNextAcquisitionCycle(acquisitionContext, acquisitionStrategy);
    isJobAdded = false;
  }

}
