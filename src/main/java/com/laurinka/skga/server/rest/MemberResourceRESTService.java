package com.laurinka.skga.server.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import com.laurinka.skga.server.model.Result;
import com.laurinka.skga.server.rest.model.Hcp;
import com.laurinka.skga.server.rest.model.NameNumberXml;
import com.laurinka.skga.server.scratch.SkgaGolferNumber;
import com.laurinka.skga.server.services.SkgaWebsiteService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@Path("/members")
@RequestScoped
public class MemberResourceRESTService {
	@Inject
	private EntityManager em;

	@Inject
	private SkgaWebsiteService service;

	@GET
	@Produces("text/xml")
	public List<NameNumberXml> listAllMembers() {

		List<NameNumberXml> results = em
				.createQuery(
						"select new com.laurinka.skga.server.rest.model.NameNumberXml(m.name, m.nr) from SkgaNumber m order by m.name", //
						NameNumberXml.class).getResultList();
		return results;
	}

	@GET
	@Path("/{nr:[0-9][0-9]*}")
	@Produces("text/xml")
	public Hcp lookupMemberById(@PathParam("nr") String aNr) {
		Result query = null;
		query = service.findDetail(new SkgaGolferNumber(aNr));
		if (null == query)
			throw new WebApplicationException();

		Hcp hcp = new Hcp();
		hcp.setHandicap(query.getHcp());
		hcp.setNumber(query.getSkgaNr());
		hcp.setName(query.getName());
		hcp.setClub(query.getClub());
		return hcp;
	}
}
