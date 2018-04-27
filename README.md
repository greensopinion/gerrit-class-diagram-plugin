Overview
========

This project provides a plug-in for [Gerrit](https://www.gerritcodereview.com/) code reviews, that adds a UML static class diagram to reviews.

This is an experiment to see if generated diagrams could be useful.  The goal is to surface abstractions and bring in context that's not available in a file- and line-based code review system.

Status
======

This is an experiment.  The code makes lots of assumptions, and is not thoroughly tested.  Feel free to try it out, but use at your own risk.

Example
=======


<a href="https://raw.githubusercontent.com/greensopinion/gerrit-class-diagram-plugin/master/doc/example.png" target="_blank"><img src="https://raw.githubusercontent.com/greensopinion/gerrit-class-diagram-plugin/master/doc/example.png" style="max-width: 600px"/></a>

Classes included in the Git commit are shown in black, while related types that are not changed in the commit are shown in dark gray.

How It Works
============

The plug-in consists of two main components:
* A Gerrit UI extension that adds an HTML image tag to the review screen
* A servlet to serve up images

The image tag includes the project name and commit hash in the URL.  The servlet uses this with JGit to retrieve the content of files in the code review, and parses them using a [Java parser](http://javaparser.org).  The resulting ASTs are used to create a [GraphViz Dot](https://www.graphviz.org) representation of a diagram, which is then sent to a [GraphViz server](https://hub.docker.com/r/omerio/graphviz-server) to generate an SVG.


How To Use
==========

Use Maven to build:

```
$ mvn package
```

Use Docker to bring up an environment:

```
$ cd docker
$ docker-compose up
```


License
=======

Copyright 2018 Tasktop Technologies Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.