/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.extensions.wizard.dynamic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.extensions.wizard.AbstractWizardModel;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.WizardModel;

/**
 * Wizard model that is specialized on dynamic wizards. Unlike the default,
 * static {@link WizardModel wizard model}, this model isn't very intelligent,
 * but rather delegates much of the work and knowledge to the
 * {@link IDynamicWizardStep dynamic wizard steps} it uses.
 * 
 * @author eelcohillenius
 */
public class DynamicWizardModel extends AbstractWizardModel
{

	private static final long serialVersionUID = 1L;

	/**
	 * The current step. The only step that matters really,
	 */
	private IDynamicWizardStep activeStep;

	/**
	 * Holds initialized steps.
	 */
	private final Set initializedSteps = new HashSet();

	/**
	 * Remember the first step for resetting the wizard.
	 */
	private final IDynamicWizardStep startStep;

	/**
	 * Construct.
	 * 
	 * @param startStep
	 *            first step in the wizard
	 */
	public DynamicWizardModel(IDynamicWizardStep startStep)
	{
		setActiveStep(startStep);
		this.startStep = startStep;
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#getActiveStep()
	 */
	public IWizardStep getActiveStep()
	{
		return activeStep;
	}

	/**
	 * @return the step this wizard was constructed with (starts the wizard).
	 *         Will be used for resetting the wizard, unless you override
	 *         {@link #reset()}.
	 */
	public final IDynamicWizardStep getStartStep()
	{
		return startStep;
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#isLastAvailable()
	 */
	public boolean isLastAvailable()
	{
		return activeStep.isLastAvailable();
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#isLastStep(org.apache.wicket.extensions.wizard.IWizardStep)
	 */
	public boolean isLastStep(IWizardStep step)
	{
		return ((IDynamicWizardStep)step).isLastStep();
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#isNextAvailable()
	 */
	public boolean isNextAvailable()
	{
		return activeStep.isNextAvailable();
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#isPreviousAvailable()
	 */
	public boolean isPreviousAvailable()
	{
		return activeStep.isPreviousAvailable();
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#last()
	 */
	public void last()
	{
		setActiveStep(activeStep.last());
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#next()
	 */
	public void next()
	{
		setActiveStep(activeStep.next());
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#previous()
	 */
	public void previous()
	{
		setActiveStep(activeStep.previous());
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#reset()
	 */
	public void reset()
	{
		setActiveStep(startStep);
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IWizardModel#stepIterator()
	 */
	public Iterator stepIterator()
	{
		return null;
	}

	/**
	 * Prepares a step for activation. Steps typically are initialized (have
	 * their method
	 * {@link IWizardStep#init(org.apache.wicket.extensions.wizard.IWizardModel)}
	 * called) the first time they are about to be displayed (thus by default
	 * when the next button is pushed or when the wizard is starting for the
	 * first step in the model). You can override this method you need more
	 * control.
	 * 
	 * @param step
	 *            The step to be activated
	 */
	protected void prepareStep(IDynamicWizardStep step)
	{
		if (!wasStepInitialized(step))
		{
			step.init(this);
		}
	}

	/**
	 * Sets the active step.
	 * 
	 * @param step
	 *            the new active step step.
	 */
	protected final void setActiveStep(IDynamicWizardStep step)
	{
		if (step == null)
		{
			throw new IllegalArgumentException("argument step must to be not null");
		}

		this.activeStep = step;

		fireActiveStepChanged(step);
	}

	protected final boolean wasStepInitialized(IDynamicWizardStep step)
	{
		return initializedSteps.contains(step);
	}

}
