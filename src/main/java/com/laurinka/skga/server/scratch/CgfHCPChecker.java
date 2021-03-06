package com.laurinka.skga.server.scratch;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.laurinka.skga.server.model.Result;

public class CgfHCPChecker {
	Logger log = Logger.getLogger(CgfHCPChecker.class.getName());

	final String ID_HCP = "ctl00_MainPlaceHolder_lbHcp";
	final String ID_NR = "ctl00_MainPlaceHolder_lbMemberNumber";
	final String ID_CLUB = "ctl00_MainPlaceHolder_lbClub";
	
	public Result query(CgfGolferNumber nr) throws IOException {
		String url = "http://www.cgf.cz/CheckHcp.aspx?MemberNumber=" + nr.asString()
				;
		final Connection connect = Jsoup.connect(url).timeout(Constants.TIMEOUT_IN_SECONDS);
		connect.header("Accept-Charset", "utf-8");
		Document document = null;
		try {
			 document = connect.get();
		} catch (SocketTimeoutException ste) {
			return null;
		}
		
		if (!isValid(document))
			return null;
		Result result = Result.newCgf();
		findHcp(document, result);
		findNumber(document, result);
		findClub(document, result);
		findName(document, result);
		return result;
	}

	private boolean isValid(Document document) {
		Element elementById = document.getElementById("ctl00_lblMessage");
		return elementById == null;
	}

	private void findName(Document document, Result result) {
		Element header = document.getElementById("ctl00_divHeader");
		Iterator<Element> iterator = header.getAllElements().iterator();
		Element next =
				iterator.next();
		Element next2 = iterator.next();
		Elements bs = next2.getElementsByTag("b");
		if (!bs.iterator().hasNext())
			return;
		Element b = bs.iterator().next();
		String text = b.text();

		result.setName(text);
	}

	private Result findHcp(Document document, Result result) {
		String hcp = document.getElementById(
				ID_HCP).text();
		if ("".equals(hcp))
			return result;
		result.setHcp(hcp);
		return result;
	}

	private void findNumber(Document document, Result result) {
		String skganr = document.getElementById(
				ID_NR).text();
		result.setSkgaNr(skganr);
	}

	private void findClub(Document document, Result result) {
		String club = document.getElementById(ID_CLUB).text();
		result.setClub(club);
	}

}
