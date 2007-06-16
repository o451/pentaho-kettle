 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/

 
/*
 * Created on 18-mei-2003
 *
 */

package org.pentaho.di.repository.dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.repository.PermissionMeta;
import org.pentaho.di.repository.ProfileMeta;
import org.pentaho.di.trans.step.BaseStepDialog;

import be.ibridge.kettle.core.Const;
import org.pentaho.di.core.GUIResource;
import org.pentaho.di.core.Props;
import be.ibridge.kettle.core.WindowProperty;



public class ProfileDialog extends Dialog 
{
	private ProfileMeta profile;
	
	private Shell     shell;
	private Label     wlName, wlDesc, wlPermission;
	private Text      wName, wDesc;
	private List      wPermission;
	
	private Button    wOK, wCancel;
	
	private String profileName;
	
	private Props   props;

    /**
     * @deprecated Use CT without <i>props</i> parameter
     */
    public ProfileDialog(Shell parent, int style, ProfileMeta profile, Props props)
    {
        this(parent, style, profile);
        this.props = props;
    }
    
	public ProfileDialog(Shell parent, int style, ProfileMeta profile)
	{
		super(parent, style);
		this.profile=profile;
		profileName=profile.getName();
		props=Props.getInstance();
	}
	
	public String open() 
	{
		Shell parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN );
 		props.setLook(shell);
		shell.setImage(GUIResource.getInstance().getImageLogoSmall());
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;
		
		shell.setText(Messages.getString("ProfileDialog.Dialog.Main.Title")); //$NON-NLS-1$
		shell.setLayout (formLayout);
 		
		// What's the profile name?
		wlName = new Label(shell, SWT.RIGHT); 
 		props.setLook(wlName);
		wlName.setText(Messages.getString("ProfileDialog.Label.Name"));  //$NON-NLS-1$
		wName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
 		props.setLook(wName);

		// What's the profile description?
		wlDesc = new Label(shell, SWT.RIGHT); 
 		props.setLook(wlDesc);
		wlDesc.setText(Messages.getString("ProfileDialog.Label.Description"));  //$NON-NLS-1$
		wDesc = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
 		props.setLook(wDesc);

		// What permissions are there?
		wlPermission = new Label(shell, SWT.RIGHT); 
		wlPermission.setText(Messages.getString("ProfileDialog.Label.Permissions"));  //$NON-NLS-1$
 		props.setLook(wlPermission);
		wPermission = new List(shell, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI );
 		props.setLook(wPermission);

		// Buttons
		wOK     = new Button(shell, SWT.PUSH); 
		wOK.setText(Messages.getString("System.Button.OK")); //$NON-NLS-1$
		wCancel = new Button(shell, SWT.PUSH); 
		wCancel.setText(Messages.getString("System.Button.Cancel")); //$NON-NLS-1$
		
		FormData fdlName       = new FormData(); 
		FormData fdName        = new FormData(); 
		FormData fdlDesc       = new FormData(); 
		FormData fdDesc        = new FormData(); 
		FormData fdlPermission = new FormData();
		FormData fdPermission  = new FormData();
		FormData fdOK          = new FormData();
		FormData fdCancel      = new FormData();

		fdlName.left  = new FormAttachment(0, 0);  // First one in the left top corner
		fdlName.right = new FormAttachment(middle, -margin);
		fdlName.top   = new FormAttachment(0, 0);
		wlName.setLayoutData(fdlName);
		fdName.left = new FormAttachment(middle, 0); // To the right of the label
		fdName.right= new FormAttachment(100, 0);
		fdName.top  = new FormAttachment(0, 0);
		wName.setLayoutData(fdName);

		fdlDesc.left  = new FormAttachment(0, 0);  // First one in the left top corner
		fdlDesc.right = new FormAttachment(middle, -margin);
		fdlDesc.top   = new FormAttachment(wName, 0);
		wlDesc.setLayoutData(fdlDesc);
		fdDesc.left = new FormAttachment(middle, 0); // To the right of the label
		fdDesc.right= new FormAttachment(100, 0);
		fdDesc.top  = new FormAttachment(wName, 0);
		wDesc.setLayoutData(fdDesc);

		fdlPermission.left = new FormAttachment(0,0); 
		fdlPermission.right= new FormAttachment(middle, -margin);
		fdlPermission.top  = new FormAttachment(wDesc, margin);  // below the line above
		wlPermission.setLayoutData(fdlPermission);
		fdPermission.left = new FormAttachment(middle, 0);  // right of the label
		fdPermission.right= new FormAttachment(100, 0);
		fdPermission.top  = new FormAttachment(wDesc, margin);
		wPermission.setLayoutData(fdPermission);

		// Optional: Nr of copies and Distribution	
		
		fdOK.left    = new FormAttachment(45, 0); 
		fdOK.top     = new FormAttachment(wPermission, margin*3);
		wOK.setLayoutData(fdOK);

		fdCancel.left   = new FormAttachment(wOK, margin); 
		fdCancel.top    = new FormAttachment(wPermission, margin*3);
		wCancel.setLayoutData(fdCancel);
		
		// Add listeners
		wCancel.addListener(SWT.Selection, new Listener ()
			{
				public void handleEvent (Event e) 
				{
					cancel();
				}
			}
		);
		wOK.addListener(SWT.Selection, new Listener ()
			{
				public void handleEvent (Event e) 
				{
					handleOK();
				}
			}
		);
		SelectionAdapter selAdapter=new SelectionAdapter()
			{
				public void widgetDefaultSelected(SelectionEvent e)
				{
					handleOK();	
				}
			};
		wName.addSelectionListener(selAdapter);;
		wDesc.addSelectionListener(selAdapter);;
		
		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );
		
		getData();

		BaseStepDialog.setSize(shell);

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return profileName;
	}
	
	public void dispose()
	{
		props.setScreen(new WindowProperty(shell));
		shell.dispose();
	}
	
	private void getData()
	{
		if (profile.getName()!=null) wName.setText(profile.getName());
		if (profile.getDescription()!=null) wDesc.setText(profile.getDescription());
		
		for (int i=1;i<PermissionMeta.permissionTypeDesc.length;i++)
		{
			wPermission.add(PermissionMeta.permissionTypeDesc[i]);
		}
		
		int sel[] = new int[profile.nrPermissions()];
		for (int i=0;i<profile.nrPermissions();i++)
		{
			PermissionMeta pi = profile.getPermission(i);
			// System.out.println("Permission: "+pi);
			sel[i]=pi.getType()-1;
		}
		
		wPermission.setSelection(sel);
	}

	private void cancel()
	{
		profileName=null;
		dispose();
	}
	
	public void handleOK()
	{		
		if (wName.getText().length()>0) // At LEAST we need a name!
		{
			profileName = wName.getText();
			profile.setName(wName.getText());
			profile.setDescription(wDesc.getText());
			
			profile.removeAllPermissions();
			
			int sel[] = wPermission.getSelectionIndices();
			for (int i=0;i<sel.length;i++)
			{
				String perm = wPermission.getItem(sel[i]);
				PermissionMeta perminfo = new PermissionMeta(perm);
				profile.addPermission(perminfo);
			}
			dispose();
		}
		else
		{
			MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR );
			mb.setMessage(Messages.getString("ProfileDialog.Dialog.UnexpectedError.Message")); //$NON-NLS-1$
			mb.setText(Messages.getString("ProfileDialog.Dialog.UnexpectedError.Title")); //$NON-NLS-1$
			mb.open(); 
		}
		
	}
}