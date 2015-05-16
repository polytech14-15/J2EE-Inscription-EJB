package ejb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.text.SimpleDateFormat;

import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;

import meserreurs.MonException;
import metier.Inscription;

import java.io.*;

import persistance.*;

/**
 * Message-Driven Bean implementation class for: DemandeInscriptionTopic
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jboss/exported/topic/DemandeInscriptionJmsTopic"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic") }, mappedName = "DemandeInscriptionJmsTopic")
public class DemandeInscriptionTopic implements MessageListener {

	@Resource
	private MessageDrivenContext context;

	/**
	 * Default constructor.
	 */
	public DemandeInscriptionTopic() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		boolean ok = false;

		// On gère le message récupéré dans le topic
		try {
			// On transforme le message en demande d'inscription
			if (message != null) {
				ObjectMessage objectMessage = (ObjectMessage) message;
				Inscription uneInscription = (Inscription) objectMessage
						.getObject();
				// On insère cette demande d'inscription dans la base de données
				try {
					ok = InsertionDemandeInscription(uneInscription);
				} catch (MonException e) {
					EcritureErreur(e.getMessage());
				}
			}
		} catch (JMSException jmse) {

			EcritureErreur(jmse.getMessage());
			context.setRollbackOnly();
		}

	}

	/**
	 * Permet d'enregistrer une erreur dans un fichier log
	 * 
	 * @param message
	 *            Le message d'erreur
	 */
	public void EcritureErreur(String message) {
		BufferedWriter wr;
		String nomf = "erreurs.log";
		java.util.Date madate = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm");

		try {
			// On écrit à la fin du fichier
			wr = new BufferedWriter(new FileWriter(nomf, true));
			wr.newLine();
			wr.write(sdf.format(madate));
			wr.newLine();
			wr.write(message);
			wr.close();
		} catch (FileNotFoundException ef) {
			;
		} catch (IOException eio) {
			;
		}
	}

	/**
	 * Permet d'insérer une demande d'inscription La table est auto incrémentée
	 * 
	 * @param d
	 *            La demande d'inscription à insérer
	 * @return Vrai si l'insertion s'est bien passée, Faux sinon
	 * @throws MonException
	 */
	public boolean InsertionDemandeInscription(Inscription d)
			throws MonException {
		String mysql = "";
		boolean ok = true;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String myDate = sdf.format(d.getDatenaissance()).toString();

			// On crée la requête d'insertion
			mysql = "INSERT INTO INSCRIPTION (NOMCANDIDAT , PRENOMCANDIDAT, DATENAISSANCE, ADRESSE, CPOSTAL,VILLE ) ";
			mysql = mysql + " VALUES (  \'" + d.getNomcandidat() + "\', ";
			mysql = mysql + "\' " + d.getPrenoncandidat() + "\', " + "\' "
					+ myDate + "\', ";
			mysql = mysql + "\' " + d.getAdresse() + "\', " + "\' "
					+ d.getCpostal() + "\',\' " + d.getVille() + "\' )";

			// On exécute la requête d'insertion et on ferme la connexion
			DialogueBd.insertionBD(mysql);
		} catch (MonException e) {
			ok = false;
			throw e;
		}
		return ok;
	}

}
