package cryodex.modules.xwing;

import java.util.ArrayList;
import java.util.List;

import cryodex.xml.XMLObject;
import cryodex.xml.XMLUtils;
import cryodex.xml.XMLUtils.Element;

public class XWingRound implements XMLObject {
	private List<XWingMatch> matches;
	private XWingRoundPanel panel;
	private Boolean isSingleElimination = false;
	private Boolean isRoundRobin = false;

	public XWingRound(Element roundElement, XWingTournament t) {
		this.isSingleElimination = roundElement
				.getBooleanFromChild("ISSINGLEELIMINATION");
		this.isRoundRobin=roundElement.getBooleanFromChild("ISROUNDROBIN");

		Element matchElement = roundElement.getChild("MATCHES");

		if (matchElement != null) {
			matches = new ArrayList<XWingMatch>();
			for (Element e : matchElement.getChildren()) {
				matches.add(new XWingMatch(e));
			}
		}

		this.panel = new XWingRoundPanel(t, matches);
	}

	public XWingRound(List<XWingMatch> matches, XWingTournament t,
			Integer roundNumber) {
		this.matches = matches;
		this.panel = new XWingRoundPanel(t, matches);
		this.isRoundRobin=t.getIsRoundRobin();
	}

	public List<XWingMatch> getMatches() {
		return matches;
	}

	public void setMatches(List<XWingMatch> matches) {
		this.matches = matches;
	}

	public XWingRoundPanel getPanel() {
		return panel;
	}

	public void setPanel(XWingRoundPanel panel) {
		this.panel = panel;
	}

	public void setSingleElimination(boolean isSingleElimination) {
		this.isSingleElimination = isSingleElimination;
	}
	
	public void setRoundRobin(boolean isRoundRobin) {
		this.isRoundRobin = isRoundRobin;
	}

	public boolean isSingleElimination() {
		return isSingleElimination == null ? false : isSingleElimination;
	}

	public boolean isRoundRobin() {
		return isRoundRobin== null ? false : isRoundRobin;
	}
	@Override
	public StringBuilder appendXML(StringBuilder sb) {

		XMLUtils.appendObject(sb, "ISSINGLEELIMINATION", isSingleElimination());
		XMLUtils.appendObject(sb, "ISROUNDROBIN", isRoundRobin());
		XMLUtils.appendList(sb, "MATCHES", "MATCH", getMatches());

		return sb;
	}

	public boolean isComplete() {
		for (XWingMatch m : getMatches()) {
			if (m.isMatchComplete() == false) {
				return false;
			}
		}
		return true;
	}

	public boolean isValid(boolean isSingleElimination) {
		boolean result = true;
		for (XWingMatch m : getMatches()) {
			if (m.isValidResult(isSingleElimination) == false) {
				result = false;
				break;
			}
		}

		panel.markInvalid(isSingleElimination);

		return result;
	}
}
