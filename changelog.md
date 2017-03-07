---
title: Changelog
last_updated: Mar 07, 2017
keywords: changelog, change log, changes, version history
---

All notable changes to this project will be documented here.

## upcoming release
<div style="margin-left: 20px">
  <b>Added</b>
  <ul>
    <li>binary memory representation</li>
    <li>binary value update mode for memory</li>
  </ul>
  <b>Changed</b>
  <ul>
    <li>increased readability of exported HTML signal table</li>
    <li>export pretty formatted HTML instead of one single line</li>
  </ul>
</div>

## 1.1.1 - 2017-02-17
<div style="margin-left: 20px">
  <b>Fixed</b>
  <ul>
    <li>MemoryTable update after memory write access did not work correctly</li>
  </ul>
</div>

## 1.1.0 - 2016-08-29
<div style="margin-left: 20px">
  <b>Added</b>
  <ul>
    <li>undo / redo feature</li>
    <li>language selection (English / German)</li>
  </ul>

  <b>Changed</b>
  <ul>
    <li>HTML signal table exporter now uses template file and HTML5</li>
    <li>Now using Java's Properties class for application configuration</li>
  </ul>
  
  <b>Fixed</b>
  <ul>
    <li>some GUI elements were not correctly reset after loading or creating a project</li>
    <li>cancel action of waiting dialogs was not processed if the dialog was closed via pressing escape</li>
    <li>machine schematics exported as JPG did not have a white background</li>
    <li>button labels for adding and removing multiplexer connections were not localized</li>
    <li>button icon for adding a register was not the correct one</li>
    <li>multiplexer selection code in MuxView was extended too early</li>
    <li>machine configuration menu item was not localized</li>
  </ul>
</div>

## 1.0.1 - 2015-10-15
<div style="margin-left: 20px">
  <b>Fixed</b>
  <ul>
    <li>machine schematics were not updated on changes</li>
    <li>base machine did not match the base machine from the lectures</li>
  </ul>
</div>

## 1.0.0 - 2015-10-09
<div style="margin-left: 20px">
  <b>Changed</b>
  <ul>
    <li>replaced the Swing GUI with a JavaFX GUI</li>
    <li>projects are saved as JSON instead of XML</li>
  </ul>
  
  <b>Added</b>
  <ul>
    <li>the following attributes are updated after changes of signal rows:
      <ul>
	<li>ALU operation codes</li>
	<li>ALU selection codes</li>
	<li>Jump targets</li>
      </ul>
    </li>
    
  </ul>  
</div>
