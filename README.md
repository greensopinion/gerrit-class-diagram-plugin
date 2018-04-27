Overview
========

This project provides a plug-in for [Gerrit](https://www.gerritcodereview.com/) code reviews, that adds a UML static class diagram to reviews.

This is an experiment to see if generated diagrams could be useful.  The goal is to surface abstractions and bring in context that's not available in a file- and line-based code review system.


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