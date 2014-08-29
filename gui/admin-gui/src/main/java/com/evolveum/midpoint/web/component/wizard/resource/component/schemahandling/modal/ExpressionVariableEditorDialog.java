/*
 * Copyright (c) 2010-2013 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.midpoint.web.component.wizard.resource.component.schemahandling.modal;

import com.evolveum.midpoint.web.component.AjaxSubmitButton;
import com.evolveum.midpoint.web.component.form.DropDownFormGroup;
import com.evolveum.midpoint.web.component.form.TextAreaFormGroup;
import com.evolveum.midpoint.web.component.form.TextFormGroup;
import com.evolveum.midpoint.web.component.util.LoadableModel;
import com.evolveum.midpoint.web.component.wizard.resource.dto.ExpressionVariableDefinitionTypeDto;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ExpressionVariableDefinitionType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectReferenceType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author shood
 * */
public class ExpressionVariableEditorDialog extends ModalWindow{

    private static final String ID_MAIN_FORM = "mainForm";
    private static final String ID_NAME = "name";
    private static final String ID_DESCRIPTION = "description";
    private static final String ID_PATH = "path";
    private static final String ID_OBJECT_REFERENCE = "objectReference";
    private static final String ID_VALUE = "value";
    private static final String ID_BUTTON_SAVE = "saveButton";
    private static final String ID_BUTTON_CANCEL = "cancelButton";

    private static final String ID_LABEL_SIZE = "col-md-4";
    private static final String ID_INPUT_SIZE = "col-md-8";

    private boolean initialized;
    private IModel<ExpressionVariableDefinitionTypeDto> model;

    public ExpressionVariableEditorDialog(String id, final IModel<ExpressionVariableDefinitionType> variable){
        super(id);

        model = new LoadableModel<ExpressionVariableDefinitionTypeDto>(false) {

            @Override
            protected ExpressionVariableDefinitionTypeDto load() {
                if(variable != null){
                    return new ExpressionVariableDefinitionTypeDto(variable.getObject());
                } else {
                    return new ExpressionVariableDefinitionTypeDto(new ExpressionVariableDefinitionType());
                }
            }
        };

        setOutputMarkupId(true);
        setTitle(createStringResource("ExpressionVariableEditor.label"));
        showUnloadConfirmation(false);
        setCssClassName(ModalWindow.CSS_CLASS_GRAY);
        setCookieName(MappingEditorDialog.class.getSimpleName() + ((int) (Math.random() * 100)));
        setInitialWidth(450);
        setInitialHeight(550);
        setWidthUnit("px");

        WebMarkupContainer content = new WebMarkupContainer(getContentId());
        content.setOutputMarkupId(true);
        setContent(content);
    }

    public void updateModel(AjaxRequestTarget target, ExpressionVariableDefinitionType variable){
        model.setObject(new ExpressionVariableDefinitionTypeDto(variable));
        target.add(getContent());
    }

    public StringResourceModel createStringResource(String resourceKey, Object... objects) {
        return new StringResourceModel(resourceKey, this, null, resourceKey, objects);
    }

    @Override
    protected void onBeforeRender(){
        super.onBeforeRender();

        if(initialized){
            return;
        }

        initLayout((WebMarkupContainer) get(getContentId()));
        initialized = true;
    }

    public void initLayout(WebMarkupContainer content){
        Form form = new Form(ID_MAIN_FORM);
        form.setOutputMarkupId(true);
        content.add(form);

        //TODO - shouldn't this be some AutoCompleteField? If yer, where do we get value?
        TextFormGroup name = new TextFormGroup(ID_NAME, new PropertyModel<String>(model, ExpressionVariableDefinitionTypeDto.F_VARIABLE + ".name.localPart"),
                createStringResource("ExpressionVariableEditor.label.name"), ID_LABEL_SIZE, ID_INPUT_SIZE, false);
        form.add(name);

        TextAreaFormGroup description = new TextAreaFormGroup(ID_DESCRIPTION,
                new PropertyModel<String>(model, ExpressionVariableDefinitionTypeDto.F_VARIABLE + ".description"),
                createStringResource("ExpressionVariableEditor.label.description"), ID_LABEL_SIZE, ID_INPUT_SIZE, false);
        form.add(description);

        TextFormGroup path = new TextFormGroup(ID_PATH, new PropertyModel<String>(model, ExpressionVariableDefinitionTypeDto.F_PATH),
                createStringResource("ExpressionVariableEditor.label.path"), ID_LABEL_SIZE, ID_INPUT_SIZE, false);
        form.add(path);

        DropDownFormGroup objectReference = new DropDownFormGroup<>(ID_OBJECT_REFERENCE,
                new PropertyModel<ObjectReferenceType>(model, ExpressionVariableDefinitionTypeDto.F_VARIABLE + ".objectRef"),
                new AbstractReadOnlyModel<List<ObjectReferenceType>>() {

                    @Override
                    public List<ObjectReferenceType> getObject() {
                        return createObjectReferenceList();
                    }
                }, new IChoiceRenderer<ObjectReferenceType>() {

            @Override
            public Object getDisplayValue(ObjectReferenceType object) {
                return createReferenceDisplayString();
            }

            @Override
            public String getIdValue(ObjectReferenceType object, int index) {
                return Integer.toString(index);
            }
        }, createStringResource("ExpressionVariableEditor.label.objectRef"), ID_LABEL_SIZE, ID_INPUT_SIZE, false);
        form.add(objectReference);

        TextAreaFormGroup value = new TextAreaFormGroup(ID_VALUE, new PropertyModel<String>(model, ExpressionVariableDefinitionTypeDto.F_VALUE),
                createStringResource("ExpressionVariableEditor.label.value"), ID_LABEL_SIZE, ID_INPUT_SIZE, false);
        form.add(value);

        AjaxSubmitButton cancel = new AjaxSubmitButton(ID_BUTTON_CANCEL,
                createStringResource("ExpressionVariableEditor.button.cancel")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                cancelPerformed(target);
            }
        };
        form.add(cancel);

        AjaxSubmitButton save = new AjaxSubmitButton(ID_BUTTON_SAVE,
            createStringResource("ExpressionVariableEditor.button.save")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                savePerformed(target);
            }
        };
        form.add(save);
    }

    //TODO - implement this - Reference to what kind of object do we need
    private List<ObjectReferenceType> createObjectReferenceList(){
        return new ArrayList<>();
    }

    //TODO -//-
    private String createReferenceDisplayString(){
        return null;
    }

    private void cancelPerformed(AjaxRequestTarget target){
        close(target);
    }

    private void savePerformed(AjaxRequestTarget target){
        //TODO - implement this
    }

}