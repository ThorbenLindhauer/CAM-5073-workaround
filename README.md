CAM-5073 workaround
===================

This project implements a workaround for Camunda BPM bug [CAM-5073](https://app.camunda.com/jira/browse/CAM-5073).

It fixes the job acquisition in order to reset job notifications after every acqusition cycle. To integrate this acquisition into the engine, the job executor must be replaced.

How to use
----------

1. Build with `mvn clean install`
2. Make the resulting jar available on the process engine's classpath
3. Configure the process engine accordingly (see sections below)

### Embedded Engine

In `camunda.cfg.xml`, declare a job executor bean of the type `org.camunda.bpm.workaround.FixedDefaultJobExecutor` and set it as the job executor property on your process engine configuration before building the process engine.

### Shared Engine

In `bpm-platform.xml`, make sure to declare the job executor class name for your job acquisitions:

```
<?xml version="1.0" encoding="UTF-8"?>
<bpm-platform xmlns="http://www.camunda.org/schema/1.0/BpmPlatform"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.camunda.org/schema/1.0/BpmPlatform http://www.camunda.org/schema/1.0/BpmPlatform ">

  <job-executor>
    <job-acquisition name="default">
      <job-executor-class>org.camunda.bpm.workaround.FixedRuntimeContainerJobExecutor</job-executor-class>
    </job-acquisition>
  </job-executor>

  ...
</bpm-platform>
```

This may require to externalize the `bpm-platform.xml` configuration files as described in the [Camunda documentation](https://docs.camunda.org/manual/7.4/reference/deployment-descriptors/descriptors/bpm-platform-xml/#configure-location-of-the-bpm-platform-xml-file).