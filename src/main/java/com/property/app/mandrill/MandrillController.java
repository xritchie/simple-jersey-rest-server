package com.property.app.mandrill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVar;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVarBucket;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.RecipientMetadata;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.property.db.entities.account.Account;
import com.property.exceptions.InternalServerException;

@Component("mandrillController")
@Qualifier("default")
@Scope("singleton")
public class MandrillController {
	
	private Boolean sendAsynch = false;
	private MandrillFactory mandrill = null;
	
	public MandrillController()
	{
		this.mandrill = new MandrillFactory();
	}
	
	@PostConstruct
	public void init()
	{
	}
	
	@PreDestroy
	public void destroy()
	{
	}
	
	private MandrillMessageStatus[] sendTemplate
	(
			String sendFrom,
			String subject, 
			List<Recipient> recipients, 
			List<MergeVar> globalMergeVars,
			List<MergeVarBucket> mergeVars,
			List<MandrillMessage.RecipientMetadata> recipientMetadata,
			List<String> tags,
			MandrillTemplates template,
			Map<String, String> templateContent
	) throws MandrillApiError, IOException
	{
		if ((recipients != null) && (recipients.isEmpty()))
			throw new InternalServerException();
			
		// create your message
		MandrillMessage message = new MandrillMessage();
		
		if (subject != null)
			message.setSubject(subject);
		
		if (sendFrom != null)
			message.setFromEmail(sendFrom);
		
		message.setFromName("Freehold.com.mt");
		
		message.setTo(recipients);
		message.setPreserveRecipients(false);
		
		message.setGlobalMergeVars(globalMergeVars);
		
		message.setMerge(true);
		message.setMetadata(templateContent);
		message.setMergeVars(mergeVars);
		message.setRecipientMetadata(recipientMetadata);
		
		if ((tags != null) && (!tags.isEmpty()))
			message.setTags(tags);

		return
			this.mandrill.getMandrillApi().messages().sendTemplate
			(
					template.toString(), 
					templateContent, 
					message,
					sendAsynch
			);
	}

	private MandrillMessageStatus[] sendTemplate
	(
			List<Recipient> recipients, 
			List<MergeVar> globalMergeVars,
			List<MergeVarBucket> mergeVars,
			List<String> tags,
			MandrillTemplates template
	) throws MandrillApiError, IOException
	{
		return
			sendTemplate
			(
					null,
					null, 
					recipients, 
					globalMergeVars,
					mergeVars,
					generateActivationMetadata(mergeVars),
					tags,
					template,
					null
			);
	}
	
	private static Recipient generateRecipient(Account account)
	{
		Recipient recipient = new Recipient();
		recipient.setEmail(account.getEmail());
		recipient.setName(account.getFirstName() + " " + account.getLastName());
		return recipient;
	}
	
	private static List<RecipientMetadata> generateActivationMetadata(List<MergeVarBucket> buckets)
	{
		List<RecipientMetadata> results = new ArrayList<RecipientMetadata>();
		
		for(MergeVarBucket bucket : buckets)
		{
			RecipientMetadata recipientMetadata = new RecipientMetadata();
			recipientMetadata.setRcpt(bucket.getRcpt());
			
			Map<String, String> values = new HashMap<String, String>();
			for (MergeVar var : bucket.getVars())
				values.put(var.getName(), var.getContent());
			recipientMetadata.setValues(values);
			
			results.add(recipientMetadata);
		}
		
		return results;
	}
	
	
	private static MergeVarBucket generateActivationMergeVarBuckets(Account account)
	{
		MergeVarBucket mergeVarBucket = new MergeVarBucket();
		mergeVarBucket.setRcpt(account.getEmail());
		mergeVarBucket.setVars
		(
				new MergeVar[]
				{
					new MergeVar("FIRST", account.getFirstName()),
					new MergeVar("LAST", account.getLastName()),
					new MergeVar("ACCOUNTID", account.getId().toString()),
					new MergeVar("ACTIVATIONCODE", account.getSingleAccessToken())
				}
		);
		
		return mergeVarBucket;
	}
	 
	@SuppressWarnings("serial")
	public MandrillMessageStatus[] sendActivationEmail(Account account) 
			throws MandrillApiError, IOException
	{
		return
			sendTemplate
			(
					new ArrayList<Recipient>() {{ add(generateRecipient(account)); }}, 
					null,
					new ArrayList<MergeVarBucket>() {{ add(generateActivationMergeVarBuckets(account)); }},
					new ArrayList<String>() {{ add(MandrillTemplates.ACTIVATION.toString()); }},
					MandrillTemplates.ACTIVATION
			);
	}


	private static MergeVarBucket generateWelcomeMergeVarBuckets(Account account)
	{
		MergeVarBucket mergeVarBucket = new MergeVarBucket();
		mergeVarBucket.setRcpt(account.getEmail());
		mergeVarBucket.setVars
		(
				new MergeVar[]
				{
					new MergeVar("FIRST", account.getFirstName()),
					new MergeVar("LAST", account.getLastName())
				}
		);
		
		return mergeVarBucket;
	}
	 
	@SuppressWarnings("serial")
	public MandrillMessageStatus[] sendWelcomeEmail(Account account) 
			throws MandrillApiError, IOException
	{		
		return
			sendTemplate
			(
					new ArrayList<Recipient>() {{ add(generateRecipient(account)); }}, 
					null,
					new ArrayList<MergeVarBucket>() {{ add(generateWelcomeMergeVarBuckets(account)); }},
					new ArrayList<String>() {{ add(MandrillTemplates.WELCOME.toString()); }},
					MandrillTemplates.WELCOME
			);
	}

	
	private static MergeVarBucket generateForgotPasswordMergeVarBuckets(Account account)
	{
		MergeVarBucket mergeVarBucket = new MergeVarBucket();
		mergeVarBucket.setRcpt(account.getEmail());
		mergeVarBucket.setVars
		(
				new MergeVar[]
				{
					new MergeVar("FIRST", account.getFirstName()),
					new MergeVar("LAST", account.getLastName()),
					new MergeVar("ACCOUNTID", account.getId().toString()),
					new MergeVar("RESETCODE", account.getSingleAccessToken())
				}
		);
		
		return mergeVarBucket;
	}
	 
	@SuppressWarnings("serial")
	public MandrillMessageStatus[] sendForgotPasswordEmail(Account account) 
			throws MandrillApiError, IOException 
	{
		return
			sendTemplate
			(
					new ArrayList<Recipient>() {{ add(generateRecipient(account)); }}, 
					null,
					new ArrayList<MergeVarBucket>() {{ add(generateForgotPasswordMergeVarBuckets(account)); }},
					new ArrayList<String>() {{ add(MandrillTemplates.FORGOTPASSWORD.toString()); }},
					MandrillTemplates.FORGOTPASSWORD
			);
	}

		 
	@SuppressWarnings("serial")
	public MandrillMessageStatus[] sendResetPasswordEmail(Account account) throws MandrillApiError, IOException
	{
		return
			sendTemplate
			(
					new ArrayList<Recipient>() {{ add(generateRecipient(account)); }}, 
					null,
					new ArrayList<MergeVarBucket>() {{ add(generateWelcomeMergeVarBuckets(account)); }},
					new ArrayList<String>() {{ add(MandrillTemplates.PASSWORDRESET.toString()); }},
					MandrillTemplates.PASSWORDRESET
			);
	}
	
	@SuppressWarnings("serial")
	public MandrillMessageStatus[] sendDeleteAccount(Account account) 
			throws MandrillApiError, IOException
	{
		return
			sendTemplate
			(
					new ArrayList<Recipient>() {{ add(generateRecipient(account)); }}, 
					null,
					new ArrayList<MergeVarBucket>() {{ add(generateActivationMergeVarBuckets(account)); }},
					new ArrayList<String>() {{ add(MandrillTemplates.ACCOUNTDELETED.toString()); }},
					MandrillTemplates.ACCOUNTDELETED
			);
	}
}
