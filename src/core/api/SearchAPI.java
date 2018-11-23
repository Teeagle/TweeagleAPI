package core.api;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import core.indexer.QueryProcessing;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Application;

@Path("/search")
public class SearchAPI {

	// The following annotations are not required. Everything is optional
	@POST
	@Path("/text")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	/***
	 * POST API request for text Queries
	 * Rank [0,1,2] = [both,VSM,TweetRANK]
	 * @param query
	 * @param ranking
	 * @return
	 */
	public String textSearch(@FormParam("q") String query, @FormParam("r") Integer ranking) {
		QueryProcessing qp = new QueryProcessing();
		System.out.print("Text to search: " + query + " with ranking set to " + ranking);
		return new Gson().toJson(qp.textSearch(query, ranking));
	}
	
	@POST
	@Path("/phrase")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	/***
	 * POST API request for Phrase Queries
	 * Rank [0,1,2] = [both,VSM,TweetRANK]
	 * @param query
	 * @param ranking
	 * @return
	 */
	public String phraseSearch(@FormParam("q") String query, @FormParam("r") Integer ranking) {
		QueryProcessing qp = new QueryProcessing();
		System.out.print("Text to search: " + query + " with ranking set to " + ranking);
		return new Gson().toJson(qp.phraseSearch(query, ranking));
	}

}
