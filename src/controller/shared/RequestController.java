package controller.shared;

import model.schedule.RequestSchedule;
import view.common.MainFrame;
import view.common.RequestForm;

public class RequestController {
    public RequestController(RequestSchedule requestSchedule) {
        RequestForm requestForm = new RequestForm(requestSchedule);
        MainFrame.addContentPanel(requestForm, "Form");
        MainFrame.showPanel("Form");
        requestForm.setGoBackOnClick(e ->{
            onGoBack();
        });

        requestForm.setSubmitOnClick(e ->{
            onSubmit();
        });
    }

    void onGoBack(){
        MainFrame.showPanel("Schedule");
    }

    void onSubmit(){

    }
}
