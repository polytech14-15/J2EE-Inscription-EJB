package metier;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

import meserreurs.MonException;
import persistance.DialogueBd;

/**
 * Classe permettant de g�rer des demandes d'inscriptions
 * @author Franck
 *
 */
public class Inscription  implements  Serializable  
{
	private static final long serialVersionUID = 1L;
	private String nomcandidat;
	private String prenomcandidat;
	private Date datenaissance;
	private String adresse;
	private String cpostal;
	private String ville;

	/**
	 * Permet de r�cup�rer l'adresse du candidat
	 * @return L'adresse du candidat
	 */
	public String getAdresse() 
	{return adresse;}

	/**
	 * Permet de sp�cifier la nouvelle valeur de l'adresse du candidat
	 * @param adresse La nouvelle valeur de l'adresse du candidat
	 */
	public void setAdresse(String adresse) 
	{this.adresse = adresse;}

	/**
	 * Permet de r�cup�rer le code postal du candidat
	 * @return Le code postal du candidat
	 */
	public String getCpostal() 
	{return cpostal;}

	/**
	 * Permet de sp�cifier la nouvelle valeur du code postal du candidat
	 * @param cpostal La nouvelle valeur du code postal du candidat
	 */
	public void setCpostal(String cpostal) 
	{this.cpostal = cpostal;}

	/**
	 * Permet de r�cup�rer la date de naissance du candidat
	 * @return La date de naissance du candidat
	 */
	public Date getDatenaissance() 
	{return datenaissance;}

	/**
	 * Permet de sp�cifier la nouvelle valeur de la date de naissance du candidat
	 * @param datenaissance La nouvelle valeur de la date de naissance du candidat
	 */
	public void setDatenaissance(Date datenaissance) 
	{this.datenaissance = datenaissance;}

	/**
	 * Permet de r�cup�rer le nom du candidat
	 * @return Le nom du candidat
	 */
	public String getNomcandidat() 
	{return nomcandidat;}

	/**
	 * Permet de sp�cifier la nouvelle valeur du nom du candidat
	 * @param nomcandidat La nouvelle valeur du nom du candidat
	 */
	public void setNomcandidat(String nomcandidat) 
	{this.nomcandidat = nomcandidat;}

	/**
	 * Permet de r�cup�rer le pr�nom du candidat
	 * @return Le pr�nom du candidat
	 */
	public String getPrenoncandidat() 
	{return prenomcandidat;}

	/**
	 * Permet de sp�cifier la nouvelle valeur du pr�nom du candidat
	 * @param prenoncandidat La nouvelle valeur du pr�nom du candidat
	 */
	public void setPrenoncandidat(String prenoncandidat) 
	{this.prenomcandidat = prenoncandidat;}

	/**
	 * Permet de r�cup�rer la ville du candidat
	 * @return La ville du candidat
	 */
	public String getVille() 
	{return ville;}

	/**
	 * Permet de sp�cifier la nouvelle valeur de la ville du candidat
	 * @param ville La nouvelle valeur de la ville du candidat
	 */
	public void setVille(String ville) 
	{this.ville = ville;}
	
	public String toString()
	{
		return "Demande d'inscription: \t" +
			   "- Nom = " + nomcandidat + "\t" + 
			   "- Pr�nom = " + prenomcandidat + "\t" + 
			   "- Date de naissance = " + datenaissance + "\t" + 
			   "- Adresse = " + adresse + "\t" + 
			   "- Code postal = " + cpostal + "\t" + 
			   "- Ville = " + ville;
	}
	
	/**
	 * Permet de r�cup�rer la liste des demandes d'inscription
	 * 
	 * @return La liste des demandes d'inscription
	 * @throws MonException
	 */
	public ArrayList<Inscription> recupererDmdInscription() throws MonException {
		ArrayList<Inscription> listeDmdInscription = new ArrayList<Inscription>();
		int index = 0;
		String mysql = "";
		List<Object> rs;
		try {

			// On cr�e la requ�te de s�lection
			mysql = "SELECT * " + "FROM inscription";

			// On ex�cute la requ�te
			rs = DialogueBd.lecture(mysql);

			// On ins�re toutes les demandes d'inscription (tous les candidats)
			// dans l'ArrayList

			while (index < rs.size()) {
				Inscription dmdInsc = new Inscription();

				dmdInsc.setNomcandidat(rs.get(index + 0).toString());
				dmdInsc.setPrenoncandidat(rs.get(index + 1).toString());
				DateFormat dateFormatpers = new SimpleDateFormat("yyyy-MM-dd");

				dmdInsc.setDatenaissance(dateFormatpers.parse(rs.get(index + 2)
						.toString()));
				dmdInsc.setAdresse(rs.get(index + 3).toString());
				dmdInsc.setCpostal(rs.get(index + 4).toString());
				dmdInsc.setVille(rs.get(index + 5).toString());
				index = index + 6;
				listeDmdInscription.add(dmdInsc);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new MonException(e.getMessage());
		}

		catch (MonException e) {
			throw e;
		}
		return listeDmdInscription;
	}
}

