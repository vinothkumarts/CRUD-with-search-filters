package finalyyy.portlet;

import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.studentservice.model.Actor;
import com.studentservice.service.ActorLocalServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import finalyyy.constants.FinalyyyPortletKeys;

/**
 * @author Vinoth kumar
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Finalyyy",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + FinalyyyPortletKeys.FINALYYY,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class FinalyyyPortlet extends MVCPortlet {
	
private final Log log = LogFactoryUtil.getLog(FinalyyyPortlet.class);
	
	@Override
		public void render(RenderRequest renderRequest, RenderResponse renderResponse)
				throws IOException, PortletException {
			
			long actorId = ParamUtil.getLong(renderRequest, "actorId");
			log.info("actorId::"+actorId);
			if(actorId > 0) {
				Actor actor = ActorLocalServiceUtil.fetchActor(actorId);
				renderRequest.setAttribute("actor", actor);
			}
			List<Actor> actors = (List<Actor>) renderRequest.getAttribute("actors");
			if(Validator.isNull(renderRequest.getAttribute("actors"))) {
				actors = ActorLocalServiceUtil.getActors(-1, -1);
			}
			
			renderRequest.setAttribute("total", actors.size());
			renderRequest.setAttribute("actors", actors);
			
			super.render(renderRequest, renderResponse);
		}
	
	public void save(ActionRequest request, ActionResponse response) {
		log.info("hiii");
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		long actorId = ParamUtil.getLong(request, "actorId");
		
		String actorName = ParamUtil.getString(request, "actorName");
		int age = ParamUtil.getInteger(request, "age");
		String language = ParamUtil.getString(request, "language");
		String movies = ParamUtil.getString(request, "movies");
		int criticRating = ParamUtil.getInteger(request, "criticRating");
		log.info("calling");
		if(actorId >0 ) {
			Actor actor = ActorLocalServiceUtil.fetchActor(actorId);
			actor.setActorName(actorName);
			actor.setAge(age);
			actor.setLanguages(language);
			actor.setMovies(movies);
			actor.setCriticRating(criticRating);
			ActorLocalServiceUtil.updateActor(themeDisplay, actor);
		}else {
			ActorLocalServiceUtil.addEditActor(themeDisplay, actorName, age, language, movies, criticRating);
		}
		
	}
	
	public void actorSearch(ActionRequest request, ActionResponse response) {
		
		log.info("actorSearch");
		String keywords = ParamUtil.getString(request, "keywords");
		int age = ParamUtil.getInteger(request, "age");
		String languages = ParamUtil.getString(request, "languages");
		String movies = ParamUtil.getString(request, "movies");
		int criticRating = ParamUtil.getInteger(request, "criticRating");
		log.info("keywords:::"+keywords);
		log.info("age:::"+age);
		log.info("language:::"+languages);
		log.info("movies:::"+movies);
		log.info("criticRating:::"+criticRating);
		List<Actor> actors = new ArrayList<Actor>();
		ClassLoader classLoader = getClass().getClassLoader();
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Actor.class, classLoader);
		Criterion criterion = null;
		if(keywords.equalsIgnoreCase("") && (age == 0) && languages.equalsIgnoreCase("") && movies.equalsIgnoreCase("") && (age == 0)) {
			criterion = RestrictionsFactoryUtil.like("actorName", "%");
		}else {
			if(keywords != "" ) {
				criterion = RestrictionsFactoryUtil.like("actorName", "%"+keywords+"%");
			}
			if(age >0) {
				if(keywords == "") {
					criterion = RestrictionsFactoryUtil.eq("age", age);
				}else {
					criterion = RestrictionsFactoryUtil.or(criterion , RestrictionsFactoryUtil.eq("age", age));
				}
				
			}
			if(languages != "" && Validator.isNotNull(languages)) {
				if(keywords == "" && age == 0) {
					criterion = RestrictionsFactoryUtil.like("languages","%"+languages+"%");
				}else {
					criterion = RestrictionsFactoryUtil.or(criterion , RestrictionsFactoryUtil.like("languages","%"+languages+"%"));
				}
			}
			if(movies != "" && Validator.isNotNull(movies)) {
				if(keywords == "" && age == 0 && languages == "") {
					criterion = RestrictionsFactoryUtil.like("movies","%"+movies+"%");
				}else {
					criterion = RestrictionsFactoryUtil.or(criterion , RestrictionsFactoryUtil.like("movies","%"+movies+"%"));
				}
			}
			if(criticRating >0) {
				if(keywords == "" && age == 0 && languages == "" && movies != "") {
					criterion = RestrictionsFactoryUtil.like("movies","%"+movies+"%");
				}else {
					criterion = RestrictionsFactoryUtil.or(criterion , RestrictionsFactoryUtil.eq("criticRating",criticRating));
				}
			}
		}
			dynamicQuery.add(criterion);
			actors = ActorLocalServiceUtil.dynamicQuery(dynamicQuery);
			
		log.info("actors:::11111111::"+actors.size());
		request.setAttribute("actors", actors);
	}
	

	public void delActor(ActionRequest request, ActionResponse response) {
		long actorId = ParamUtil.getLong(request, "actorId");
		
		log.info("actorId:::11::"+actorId);
		try {
			ActorLocalServiceUtil.deleteActor(actorId);
			AssetEntryLocalServiceUtil.deleteEntry(
				    Actor.class.getName(),actorId);
		} catch (PortalException e) {
			e.printStackTrace();
		}
		
	}
}