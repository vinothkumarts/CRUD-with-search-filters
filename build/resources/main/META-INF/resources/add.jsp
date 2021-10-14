<%@ include file="init.jsp" %>

<portlet:actionURL name="save" var="saveURL" />

<aui:form method="post" name="actorFm" action="${saveURL }">
	<aui:input name="actorId" type="hidden"value='${actor ne null ? actor.actorId : "" }'></aui:input>
	<aui:input name="actorName" value='${actor ne null ? actor.actorName : "" }'></aui:input>
	<aui:input name="age" value='${actor ne null ? actor.age : "" }'></aui:input>
	<aui:input name="language" value='${actor ne null ? actor.languages : "" }'></aui:input>
	<aui:input name="movies" value='${actor ne null ? actor.movies : "" }'></aui:input>
	<aui:input name="criticRating" value='${actor ne null ? actor.criticRating : "" }'></aui:input>
	<aui:button type="submit" value="save" />
</aui:form>