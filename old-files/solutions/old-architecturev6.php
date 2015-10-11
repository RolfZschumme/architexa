<?php include($_SERVER['DOCUMENT_ROOT'].'/includes/header-beg.php') ?>
<title>Overcoming Architectural Challenges in Agile</title>
<link rel="stylesheet" href="/css/prettyPhoto.css" type="text/css" media="screen" charset="utf-8" />
<?php include($_SERVER['DOCUMENT_ROOT'].'/includes/header-end.php') ?>
<?php include($_SERVER['DOCUMENT_ROOT'].'/includes/side-menu-header-learn-more.php') ?>
<script src="/js/jquery-1.3.2.js" type="text/javascript" charset="utf-8"></script>

<script src="/js/jquery.prettyPhoto.js" type="text/javascript" charset="utf-8"></script>

<!--<h1>Challenges faced in Agile implementation</h1>
<p>While the strengths of Agile development are well-known, traditional teams have faced challenges in implementing it successfully. 
Teams are often not able to have a consistent architectural view of a project or often have difficulty in adhering 
to the Agile principles.</p> 


<div style="float:right; margin: 5pt 0pt 0pt 0px;"> 
	<a href="/images/thumbnails/Agile-problem.jpg" rel="prettyPhoto" title="Agile problem" > 
	<img style="margin=0 0 0 10px;" src="/images/thumbnails/Agile-problem.jpg" width="300" height="200" border="1"/> 
	</a> 
</div> 

-->

<h1>Architectural Challenges for Agile Projects</h1>

<table style="float:right; margin: 5pt 0pt 0pt 0px;">
<tr>
<td >
<div style="float:right; margin: 5pt 0pt 0pt 0px;"> 
	<a href="/images/thumbnails/architecturalChallenges.jpg" rel="prettyPhoto" title="Architectural challenges" > 
	<img style="margin=0 0 0 10px;" src="/images/thumbnails/architecturalChallenges.jpg" width="300" height="200" border="1"/> 
	</a> 
</div>
</td>
</tr>
<tr>
<td width="300"><p align="center" style=" margin: -12pt 0pt 0pt 0px;"><font size ="2">Architectural challenges</font></p></td>
</tr>
</table>

<p>Agile projects experience constantly changing code. UML tools try 
to provide architectural support, but they are limited in that their use requires a 
significant amount of time before teams can see any benefits.</p>

<p>With changing code, developers often cannot afford to spend months updating diagrams. Out-of-date UML diagrams prevent developers from 
enjoying its benefits.</p>

<p><b>Rapid code modifications in Agile projects prevent projects from benefitting from a well-defined architecture 
and module boundaries</b> resulting in a number of problems.</p> 


<h3>Undetected missing Requirements</h3>

<p>Without a commonly understood set of components for a project shared among 
the team, developers are prevented from thoroughly examining architectural 
issues.  <b>Lack of a well-defined structure</b> for the project prevents developers from 
detecting important missing capabilities early during development, only to 
discover components missing towards the end of the project which requires 
significant time to fix and implement the missing capabilities.</p>

<h3>Lack of Component Reuse</h3>
<p>
Component reuse is widely known to be useful in reducing bugs, improving productivity, and simplifying maintenance. However, the <b>lack of a shared diagrams</b>, prevents developers from getting a complete view of the architecture of the codebase, and limits the effectiveness of planning for components reuse.
</p>

<h3>
Difficulty using shared components
</h3>
<p>With unclear module boundaries, developers do not fully understand modules usage. Both the module builders and users have no guidance as to the appropriate depedenencies and usage protocol - capabilities might be accidentally hidden and/or important dependencies might not be available to those needing to use the module.
 
It is often unclear to both the module builder of all the parameters needed for the module, and to module users as to how to get the parameters. Thus, developers face difficulty in utilizing shared components.

<!--Thus, developers face difficulty in utilizing shared components as their public methods are hidden from the developers' views. -->


<h3>Architectural Erosion and Code Overlap</h3>

<p>With multiple teams working on different parts of a code base, <b>module boundaries are often not clearly defined</b>. Without an updated view of module boundaries, developers find it hard to determine which modules newly implemented features should reside in. This often results in code being in incorrect locations, structural erosion, and code overlap across different modules. 
</p>


<!--
<h3>
Lack of code visibility
</h3>

<p>
There is often a lack of code visibility for Agile developers due to
the lack of documentation and up-to-date UML diagrams. Not only does this 
result in <a href="/challenges/code-quality">architectural deterioration</a> of a project over time, it also severely limits developers in optimizing code efficiency as they lack sufficient 
knowledge of the overall architecture.
</p>
-->

<h1>
Solving these challenges with UML straight from your code using Architexa
</h1>

<table style="float:right; margin: 5pt 0pt 0pt 0px;">
<tr>
<td >
<div style="float:right; margin: 5pt 0pt 0pt 0px;"> 
	<a href="/images/thumbnails/overcomeagilechallenge.jpg" rel="prettyPhoto" title="Overcoming challenges with Architexa" > 
	<img style="margin=0 0 0 10px;" src="/images/thumbnails/overcomeagilechallenge.jpg" width="300" height="200" border="1"/> 
	</a> 
</div>
</td>
</tr>
<tr>
<td width="300"><p align="center" style=" margin: -12pt 0pt 0pt 0px;"><font size ="2">Overcoming challenges with Architexa</font></p></td>
</tr>
</table>
<!--
<p>
To help developers overcome the architectural issues, the Architexa suite is specially designed to assist 
developers in benefitting from UML diagrams by allowing them to create UML diagrams which are automatically generated from their code
</p>
<!--
<h3>
Bringing Structure to Agile
</h3>
<p>
Architexa introduces the benefits of structured development to Agile through the use of UML diagrams. This is achieved in two ways. 
<ul>
<li> 
Architexa helps developers reverse engineer their code to generate 
diagrams so that up-to-date architectural view could be maintained. This enables Agile developers to plan 
effectively for architectural issues such as component reuse. 
</li>
<li> Code are automatically documented with diagrams, which frees developers to focus on coding and solves the problem of lack of documentation in Agile.
</li>

</ul>

</p>

-->
<table style="float:right; margin: 5pt 0pt 0pt 0px;">
<tr>
<td >
<div style="float:right; margin: 5pt 0pt 0pt 0px;"> 
	<a href="/images/thumbnails/layered3.jpg" rel="prettyPhoto" title="Module and class boundaries defined with diagrams" > 
	<img style="margin=0 0 0 10px;" src="/images/thumbnails/layered3.jpg" width="300" height="200" border="1"/> 
	</a> 
</div>
</td>
</tr>
<tr>
<td width="300"><p align="center" style=" margin: -12pt 0pt 0pt 0px;"><font size ="2">Module and class boundaries defined with diagrams</font></p></td>
</tr>
</table>

-->

<h2>Managing the challenges with Architexa</h2>

<p>
Architexa suite is specially designed to assist developers in benefitting from UML diagrams and collaboration by allowing them to create UML diagrams which are automatically generated from their code. 
</p>
<p>
To help developers in overcoming the significant architectural challenges faced, Architexa implements a complete set of extensive features to maintain well-defined architecture and module boundaries, reduce code duplication and maxmize team collaboration efficiency.
</p>

<h2>
1) Maintain a well-defined architecture and module boundaries
</h2>
<p>
To build a well-defined architecture, developers have to figure out the module boundaries and relationships and clearly define them. A deep understanding of the architecture and modules flow is required to maintain the architecture. 
</p>
<h3>
Understanding the architecture
</h3>
<p>
Architexa employs layered and class diagrams to provide a clear overview to the various components for developers to better understand the architecture. Elements and members of each package are displayed in layered diagrams, which allow developers to identify similar classes to be grouped in same module group.
</p>

<h3>
Figuring out module boundaries and relationships
</h3>
<p>
Architexa provides several ways for developers to define clear module boundaries through various features:
</p>




<h3>
Minimizing architectural erosion 
</h3>
<p>
Layered diagrams help to give developers a clear overview of module and package structure while class diagrams provide further details of the module implementation. This allows developers to determine which newly implemented feature should reside in which module, thus effectively preventing architectural erosion as new capabilities are added.
</p>

<h3>
Understanding logic flow across different modules 
</h3>
<p>
Sequence diagrams allows developers to understand the logic flow of the program and the way modules interact and help them make decisions on the architecture and module boundaries.
</p>


<h2>
2) Maximise component reuse and reduce code duplication
</h2>
<p>
Architexa can help to identify shared component for reuse and avoid unnecessary code duplication.
</p>
<h3>
Identify shared components and code duplication
</h3>
<p>
Common components could be identified with a clearer understanding the logical flow of the code using sequence diagrams.  Code duplication could be identified in different modules using architectural diagrams built using class and layered diagrams, which helps prevent code overlap and promote code reuse. Shared dependencies pointing to the same modules are highlighted to help developers identify potential shared component for reuse.
</p>

<h3>
Utilize shared component effectively with documentation
</h3>
<p>
Class diagrams reveal public methods of the interface of shared component, which provide easily accessible reference for developers to utilize public components effectively.
</p>

<h3>
Identify missing capabilities and requirement
</h3>
<p>
Understanding the logical flow of modules and module interaction with sequence diagrams help developers to observe the program functionalities and identify missing capabilities, preventing any unpleasant development surprises such as missing software requirement from occurring in the future.  
</p>


<h2>
3)Increase team collaboration and provide effective annotation of architectural issues 
</h2>
<p>
Finally to facilitate collaboration on architectural issues, Architexa provides point of reference for component reuse and architectural discussion through the following features.
</p>
<br>
<ul>
<li> 
Class and layered diagrams can be used as code architecture documentation to provide a common ground built straight from the code.  This allows team members to identify common components and provide proper documentation so that developers can effectively deploy shared components.
</li>
<li>Diagrams annotations can be used to provide reference for component reuse and in architectural definition and discussion
</li>
<li>
Module boundaries can be clearly defined through easy diagram annotation and sharing of code diagrams
</li>
</ul>







<h3>

</h3>
<p>

</p>





<!-- 

<h4>Class Diagrams</h4>

<p>
Class Diagrams emphasize the classes, attributes, operations, and the relationships between them to provide a quickly understanding of the interconnection among elements. Through these diagrams, developers can explore the relationship between classes, define the boundaries of different modules and check for missing methods or capabilities within each class. 
</p>

<p>
Each developer can then code according to the architectural needs for a specific part of the project with clear class diagrams defining module boundaries. Code duplication could also be avoided as developers will know which features are already available easily through the diagrams.Through the diagrams, essential capabilities are clearly identified and not missed, preventing any unpleasant development surprises such as missing software requirement from occurring in the future.  
</p>

<p>
Sequence Diagrams emphasize the interaction between the given classes/objects to help developers quickly figure out the detailed logic flow of certain parts of the code-base.
</p>

<h4>Code discussion and annotation</h4>



<ul>
<li>
Code Architecture Documentation provides a common ground built straight from the code, allowing team members to identify common components and provide proper documentation so that developers can effectively deploy shared components.
</li>
<li>
Diagrams annotations can be used to provide reference for component reuse and in architectural definition and discussion
</li>
<li>
Module boundaries can be clearly defined through code discussion and annotation with diagrams
</li>
</ul>


<h3>
Meeting  software requirements
</h3>

<p>
Architectural diagrams also 
</p>

<table style="float:right; margin: 5pt 0pt 0pt 0px;">
<tr>
<td>
<div style="float:right; margin: 5pt 0pt 0pt 0px;"> 
	<a href="/images/thumbnails/relo.png" rel="prettyPhoto" title="Clear overview of methods and class" > 
	<img style="margin=0 0 0 10px;" src="/images/thumbnails/relo.png" width="300" height="200" border="1"/> 
	</a> 
</div>
</td>
</tr>
<tr>
<td width="300"><p align="center" style=" margin: -12pt 0pt 0pt 0px;"><font size = 2>Clear overview of methods and class</font></p></td>
</tr>
</table>
<h3>
Better code visibility
</h3>
-->
<!--
<p>
Diagrams allow developers to maintain better architectural tracking and code visibilty. They would be able to 
optimize code with greater code visibilty and understanding of how other code segments work. With better code visibilty, public module methods and interfaces are exposed so that developers understand how to call and utilize shared components effectively.
</p> 
-->

<!--<a href="/technology/Agile-challenge.php"> >>> Next: Overcoming challenges in follwing Agile Principles </a> -->
<br>
<script type="text/javascript" charset="utf-8">
		$(document).ready(function(){
			$("a[rel^='prettyPhoto']").prettyPhoto();

		});
</script>

<br>

<?php include($_SERVER['DOCUMENT_ROOT'].'/includes/side-menu-footer.php') ?>
<?php include($_SERVER['DOCUMENT_ROOT'].'/includes/footer.php') ?>
