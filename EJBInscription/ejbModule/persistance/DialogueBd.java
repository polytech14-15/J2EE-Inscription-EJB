package persistance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import meserreurs.MonException;

/**
 * Permet de gérer la dialogue avec la base de données
 * 
 * @author FChristian
 * 
 */
public class DialogueBd {


	private static DialogueBd instance = null;

	/**
	 * Constructeur par défaut de la classe
	 */
	public DialogueBd() {
		super();
	}

	/**
	 * Permet de retourner l'instance de cette classe (une instance unique car
	 * utiliation d'un singleton)
	 * 
	 * @return L'instance de cette classe représentant un dialogue avec la base
	 *         de données
	 */
	public static DialogueBd getInstance() {
		if (instance == null) {
			instance = new DialogueBd();
		}
		return instance;
	}

	/**
	 * Permet d'insérer une demande d'inscription
	 * 
	 * @param d
	 *            La demande d'inscription à insérer
	 * @return Vrai si l'insertion s'est bien passée, Faux sinon
	 * @throws MonException
	 */
	public static void insertionBD(String mysql) throws MonException {
		Connection cnx = null;
		try {
			cnx = Connexion.getInstance().getConnexion();
			
			Statement unstatement = cnx.createStatement();
			System.out.println("Je me prépare à insérer " + mysql);
			unstatement.execute(mysql);
			// on ferme la connexion
			cnx.close();
		} catch (SQLException e)

		{
			System.out.println("Erreur :" + e.getMessage());
			System.out.println(mysql);
			new MonException(e.getMessage());
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new MonException(e.getMessage());
		}
	}

	public static List<Object> lecture(String req) throws MonException {
		Connection cnx = null;
		Statement stmt;
		ResultSet rs;
		List<Object> mesRes = new ArrayList<Object>();
		int i;
		int nbCols;
		try {

			cnx = Connexion.getInstance().getConnexion();
			stmt = cnx.createStatement();
			stmt.executeQuery("SET NAMES UTF8");
			// Execution de la requete
			rs = stmt.executeQuery(req);
			// on retrouve le nombre de colonnes de la requête
			ResultSetMetaData rsmd = rs.getMetaData();
			nbCols = rsmd.getColumnCount();
			i = 1;
			// on balaie toutes les lignes
			while (rs.next()) {

				// On balaie les colonnes
				i = 1;
				while (i <= nbCols) {
					mesRes.add(rs.getObject(i));
					i++;
				}
			}
			cnx.close();
			// Retourner la table
			return (mesRes);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new MonException(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new MonException(e.getMessage());
		} finally {
			// S'il y a eu un problème, la connexion
			// peut être encore ouverte, dans ce cas
			// il faut la fermer.

			if (cnx != null)
				try {
					cnx.close();
				} catch (SQLException e) {
				}
		}
	}

	public static void execute(String mysql) throws MonException {
		Connection cnx = null;
		try {
			cnx = Connexion.getInstance().getConnexion();
			Statement unstatement = cnx.createStatement();
			unstatement.execute(mysql);
			System.out.println(mysql);
			// on ferme la connexion
			cnx.close();
		} catch (SQLException e)
		{
			throw new MonException(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new MonException(e.getMessage());
		}
	}
}
