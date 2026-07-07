# eGK-NFC-Communicator

## Overview

This repository contains an Android SDK which can be used to communicate with eGKs using NFC. The
code originally stems from the [Gematik E-Rezept-App](https://github.com/gematik/E-Rezept-App-Android).
Relevant sections for communication with eGKs have been extracted and very slightly adjusted, so
that they can be used as an SDK/library to abstract away low-level communication with eGKs.

---

## Building the SDK

### Prerequisites

#### Required:
<details><summary>For Building in Terminal</summary>

- A Java Runtime version 21 or higher (Depending on your system, definition of the `JAVA_HOME`
  environment variable may
  be required)
- Android SDK with the `ANDROID_HOME` environment variable set to it`s location

</details>
<details><summary>For Building with Android Studio</summary>

- Android Studio

</details>

#### Recommended:

- A Java Development Kit version 21 (will be downloaded if missing)

### Build & Publish to your Local Maven repository

<details><summary>From Terminal on Linux, MacOs, BSD or other Unix like Systems (Shell)</summary>

From within the Project root directory, for release builds call

```shell
./gradlew :sdk:publishReleasePublicationToMavenLocal
```

or Snapshots

```shell
./gradlew :sdk:publishSnapshotPublicationToMavenLocal
```

</details>
<details><summary>From Terminal on Windows (Powershell or CMD)</summary>

From within the Project root directory, for release builds call

```powershell
.\gradlew.bat :sdk:publishReleasePublicationToMavenLocal
```

or Snapshots

```shell
.\gradlew.bat :sdk:publishSnapshotPublicationToMavenLocal
```

</details>
<details><summary>In Android Studio</summary>

Select and run the `publishReleaseToMavenLocal` or `publishSnapshotToMavenLocal` Run Configuration in the top right
corner of Android Studio.
</details>

This will build and push the Sdk Maven artifact to your local Maven repository typically located
under `~/.m2`

### Build & Publish to project level maven repository

<details><summary>From Terminal on Linux, MacOs, BSD or other UnixLike Systems (Shell)</summary>

From within the Project root directory, for release builds call

```shell
./gradlew :sdk:publishReleasePublicationToProjectMavenRepository
```

or for snapshots

```shell
./gradlew :sdk:publishSnapshotPublicationToProjectMavenRepository
```

</details>
<details><summary>From Terminal on Windows (Powershell or CMD)</summary>

From within the Project root directory, for release builds call

```powershell
.\gradlew.bat :sdk:publishReleasePublicationToProjectMavenRepository
```

or for snapshots

```powershell
.\gradlew.bat :sdk:publishSnapshotPublicationToProjectMavenRepository
```

</details>
<details><summary>In Android Studio</summary>

Select and run the `publishReleaseToProjectMaven` or `publishSnapshotToProjectMaven` Run Configuration in the top right
corner of Android Studio.
</details>

This will build and push the Sdk Maven artifact to a `.m2` directory located under the Project root
directory

### Build & Publish to Artifactory

#### Prerequisites

To be able to build and publish the SDK to your Artifactory (or other artifact repository) you will
need to ensure that some variables are set either in your `local.properties` or in the `environment`,
when running in a CI-pipeline for example:

- `ARTIFACTORY_URL`: the URL of the repository the artifact should be pushed into
- `ARTIFACTORY_SNAPSHOT_URL`: the URL of the snapshot repository the artifact should be pushed into,
  if the version is a snapshot version
- `ARTIFACTORY_USER`: the User that should be used for authentication against the repository
- `ARTIFACTORY_TOKEN`: the token/password to be used for authentication against the repository

#### Build & Publish

<details><summary>From Terminal on Linux, MacOs, BSD or other UnixLike Systems (Shell)</summary>

From within the Project root directory, for release builds call

```shell
./gradlew --full-stacktrace --no-daemon :sdk:publishReleasePublicationToArtifactoryReleaseRepository
```

or for snapshots

```shell
./gradlew --full-stacktrace --no-daemon :sdk:publishSnapshotPublicationToArtifactorySnapshotRepository
```

</details>
<details><summary>From Terminal on Windows (Powershell or CMD)</summary>

From within the Project root directory, for release builds call

```powershell
.\gradlew.bat --full-stacktrace --no-daemon :sdk:publishReleasePublicationToArtifactoryReleaseRepository
```

or for snapshots

```powershell
.\gradlew.bat --full-stacktrace --no-daemon :sdk:publishSnapshotPublicationToArtifactorySnapshotRepository
```

</details>
<details><summary>In Android Studio</summary>

Select and run the `publishReleaseToArtifactory` or `publishSnapshotToArtifactory` Run Configuration in the top right
corner of Android Studio.
</details>

---
