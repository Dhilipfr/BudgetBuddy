package dhilip.code.org.budgetbuddy;

import java.util.Date;

/**
 * Created by Dhilip on 13-10-2015.
 */
public class SummaryDetails {
    Integer Id;
    Integer ActionId;
    String Description;
    Date CreatedOn,UpdatedOn;
    Double Amount;

    public void setId(Integer id){this.Id = id;}
    public Integer getId(){return this.Id;}
    public void setActionId(Integer actionId)
    {
        this.ActionId = actionId;
    }
    public Integer getActionId()
    {
        return this.ActionId;
    }
    public void setCreatedOn(Date createdOn)
    {
        this.CreatedOn = createdOn;
    }
    public Date getCreatedOn()
    {
        return  this.CreatedOn;
    }
    public void setUpdatedOn(Date updatedOn)
    {
        this.UpdatedOn = updatedOn;
    }
    public Date getUpdatedOn()
    {
        return  this.UpdatedOn;
    }
    public void setAmount(Double amount)
    {
        this.Amount = amount;
    }
    public Double getAmount()
    {
        return  this.Amount;
    }
    public void setDescription(String description)
    {
        this.Description= description;
    }
    public String getDescription()
    {
        return  this.Description;
    }
}
