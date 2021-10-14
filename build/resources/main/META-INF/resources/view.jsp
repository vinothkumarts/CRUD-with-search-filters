<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.studentservice.service.ActorLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.dao.search.DisplayTerms"%>
<%@ include file="/init.jsp"%>


<liferay-portlet:renderURL varImpl="iteratorURL">
	<portlet:param name="mvcPath" value="/view.jsp" />
</liferay-portlet:renderURL>
<liferay-portlet:renderURL varImpl="addURL">
	<portlet:param name="mvcPath" value="/add.jsp" />
</liferay-portlet:renderURL>
<portlet:resourceURL var="searchURL" />
<liferay-portlet:actionURL name="actorSearch" var="actorSearchURL">
	<portlet:param name="mvcActionCommand" value="actorSearch" />
</liferay-portlet:actionURL>
<aui:button onClick="${addURL}" value="add-actor"></aui:button>

<aui:form action="${actorSearchURL}" method="post" name="fm1">
	<liferay-ui:search-toggle buttonLabel="search"
		displayTerms="<%=new DisplayTerms(renderRequest)%>" id="advanceSearch">
		<aui:input type="text" name="age" />
		<aui:input type="text" name="languages" label="Languages" />
		<aui:input type="text" name="movies" />
		<aui:input type="text" name="criticRating" label="Critic Rating" />
	</liferay-ui:search-toggle>
</aui:form>
<liferay-ui:search-container delta="5" deltaConfigurable="true"
	emptyResultsMessage="no-actors">
	<liferay-ui:search-container-results results="${actors}" />

	<liferay-ui:search-container-row
		className="com.studentservice.model.Actor" modelVar="actor">
		<liferay-ui:search-container-column-text name="actorName"
			property="actorName" />

		<liferay-ui:search-container-column-text name="age" property="age" />

		<liferay-ui:search-container-column-text name="languages"
			property="languages" />

		<liferay-ui:search-container-column-text name="movies"
			property="movies" />
		<liferay-ui:search-container-column-text name="criticRating"
			property="criticRating" />

		<liferay-ui:search-container-column-text name="edit">
			<liferay-portlet:renderURL varImpl="editURL">
				<portlet:param name="mvcPath" value="/add.jsp" />
				<portlet:param name="actorId" value="${actor.actorId}" />
			</liferay-portlet:renderURL>
			<a href="${editURL }"><button type="button"
					class="btn btn-default btn-sm">
					<span class="glyphicon glyphicon-pencil"></span> Update
				</button> </a>
		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-text name="delete">
			<liferay-portlet:actionURL name="delActor" var="delActorURL">
				<portlet:param name="mvcActionCommand" value="delActor" />
				<portlet:param name="actorId" value="${actor.actorId}" />
			</liferay-portlet:actionURL>


			<a href="${delActorURL }"><button type="button"
					class="btn btn-default btn-sm">
					<span class="glyphicon glyphicon-remove"></span> Remove
				</button></a>
		</liferay-ui:search-container-column-text>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<%-- <aui:script>
	function search(){
	console.log("search");
	var searchText = $("#<portlet:namespace />searchText").val();
	console.log("searchText:::"+searchText);
	   	$.ajax({
	   		  url : '${searchURL}', 
	          type: 'post',
	           data: {
	       	   <portlet:namespace />searchText: searchText,
	           },
               success: function(data) {
               		console.log("dfsdfdg::::"+data);
                }
	    });
	}
</aui:script> --%>