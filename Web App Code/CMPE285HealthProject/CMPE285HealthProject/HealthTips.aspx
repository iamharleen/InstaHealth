<%@ Page Title="HealthTips" Language="C#" MasterPageFile="~/Site.MasterProject.master" AutoEventWireup="true" CodeBehind="HealthTips.aspx.cs" Inherits="CMPE285HealthProject.HealthTips" %>

<asp:Content runat="server" ID="FeaturedContent" ContentPlaceHolderID="ContentPlaceHolder1">
    <div class="top-nav">
						<div class="wrap">
							<ul>
                                <!-- to be added links-->
								<li><a href="Home.aspx">Home</a></li>
								<li><a href="Hospital.aspx">Hospital</a></li>
								<li><a href="Physician.aspx">Physician</a></li>
								<li><a href="New_Appointment.aspx">Appointment</a></li>
								<li><a href="Helpline.aspx">Helpline</a></li>
								<li><a href="Ambulance.aspx">Ambulance</a></li>
								<li class="active"><a href="HealthTips.aspx">Health Tips</a></li>
								<li><a href="Parking.aspx">Parking</a></li>
								
							</ul>
                            <div class="clear"> </div>
						</div>
					</div>
    <div class="content">
        <div class="wrap">
            <div class="content-top-grid">
                <div class="content-top-grid-header">
                    <div class="content-top-grid-menu-title">
                        <h3>Health Tips</h3>
                    </div>
                </div>
            </div>
            <div class="content-menu-grid">
                <br />
                <asp:DataList ID="DataList1" runat="server" DataKeyField="tip_id" DataSourceID="CMPE285HealthProject" RepeatColumns="1" OnSelectedIndexChanged="DataList1_SelectedIndexChanged">
                    <ItemTemplate>
                        <asp:Label ID="tip_nameLabel" runat="server" Text='<%# Eval("tip_name") %>' ForeColor="Blue" Font-Size="Large" />
                        <br />
                        <asp:Label ID="tip_descriptionLabel" runat="server" Text='<%# Eval("tip_description") %>' Font-Size="Medium" ForeColor="Black" />
                        <br />
                        <asp:Label ID="link_label" runat="server" Text="For More Details:  " Font-Bold="True" Font-Size="Medium" ForeColor="Black"></asp:Label>
                        <asp:HyperLink ID="tip_link" runat="server" Text='<%# Eval("tip_link") %>' Target="_blank" NavigateUrl='<%# Eval("tip_link") %>'></asp:HyperLink>
                        <br />
                        ----------------------------------------------------------------------------------------------------------------------<br />
                        <br />
                    </ItemTemplate>
                </asp:DataList>
                <asp:SqlDataSource ID="CMPE285HealthProject" runat="server" ConnectionString="<%$ ConnectionStrings:ConnString%>" SelectCommand="SELECT * FROM [health_tips]"></asp:SqlDataSource>

            </div>
        </div>
        <div class="clear"></div>
    </div>
</asp:Content>

