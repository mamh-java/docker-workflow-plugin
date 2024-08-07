/*
 * The MIT License
 *
 * Copyright (c) 2017, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

pipeline {
    agent {
        label "docker"
    }
    stages {
        stage("foo") {
            steps {
                sh 'ls -la'
                sh 'echo "The answer is 42"'
                sh 'echo "${NODE_NAME}" > tmp.txt'
            }
        }
        stage("bar") {
            agent {
                docker {
                    image "httpd:2.4.59"
                    reuseNode true
                }
            }
            steps {
                sh 'test -f Jenkinsfile'
                sh 'test -f tmp.txt'
                echo "found tmp.txt in bar"
            }
        }
        stage("new node - docker") {
            agent {
                docker {
                    image "httpd:2.4.59"
                }
            }
            steps {
                sh 'test -f Jenkinsfile'
                sh 'test ! -f tmp.txt'
                echo "did not find tmp.txt in new docker node"
            }
        }
        stage("new node - label") {
            agent any

            steps {
                sh 'test -f Jenkinsfile'
                sh 'test ! -f tmp.txt'
                echo "did not find tmp.txt in new label node"
            }
        }
    }
}



