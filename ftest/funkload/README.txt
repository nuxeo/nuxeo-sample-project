======================================
Funkload Sample test
======================================

Abstract
============

This module contains python funkload scripts. These scripts can be used
to do functional testing or benching of the sample apps.

Running tests
===============


Requirement
-------------

* Install latest FunkLoad snapshots, on Lenny or Intrepid Ibex this can be
  done like this::

  sudo aptitude install python-dev python-xml python-setuptools \
       python-webunit=1.3.8-1 python-docutils gnuplot
  sudo aptitude install tcpwatch-httpproxy --without-recommends
  sudo easy_install -f http://funkload.nuxeo.org/snapshots/ -U funkload


For other distro visit http://funkload.nuxeo.org/INSTALL

