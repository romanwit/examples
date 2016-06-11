<%@ Page Language="C#" EnableEventValidation="false"%>
<%@ Import Namespace="System.IO" %>
<%@ Import Namespace="System.Xml" %>
<%@ Import Namespace="System.Xml.XPath" %>
<%@ Import Namespace="System.Runtime.Serialization.Json" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<script runat="server">



private void Read(XmlReader reader)
{
    int i = 0;
    int j=0;
    String curFieldName = "";
    TableRow r = new TableRow();
    TableCell c = new TableCell();
    TableRow trh = new TableRow();
    TableHeaderCell th = new TableHeaderCell();
    while (reader.Read())
    {


                if ((reader.NodeType==XmlNodeType.Element) && (reader.LocalName=="item"))
                {
                    //begin of XML line
                    r = new TableRow();
                    if (i==0)
                    {
                        trh = new TableRow();
                    }
                }
                if ((reader.NodeType==XmlNodeType.Element) && (reader.LocalName!="item")
                    &&(reader.LocalName!="root"))
                {
                    //start of XML field
                    curFieldName = reader.Name;
                    if (i==0)
                    {
                        th = new TableHeaderCell();
                        HtmlInputButton b = new HtmlInputButton("submit");
                        b.Value = reader.Name;
                        b.ID="but"+j;
                        b.ServerClick+=new System.EventHandler(this.Button_Click);
                        th.Controls.Add(b);
                        LiteralControl lSort = new LiteralControl("<a id='sort"+j+"' runat='server'></a>");
                        th.Controls.Add(lSort);
                        j++;
                        trh.Cells.Add(th);
                    }

                }
                if (reader.NodeType==XmlNodeType.Text)
                {
                    //XML value of field
                    c = new TableCell();
                    c.Controls.Add(new LiteralControl(reader.Value));
                    r.Cells.Add(c);
                }
                if ((reader.NodeType==XmlNodeType.EndElement)&&(reader.LocalName==curFieldName))
                {
                    //XML end of field
                }
                if ((reader.NodeType==XmlNodeType.EndElement)&&(reader.LocalName=="item"))
                {
                    //end of XML line
                    if (i==0)
                    {
                        Table1.Rows.Add(trh);
                    }
                    i++;
                    Table1.Rows.Add(r);
                }

    }
}


    private void Page_Load(Object sender, EventArgs e)
    {
       byte[] buffer = File.ReadAllBytes("C:\\shit\\grid.json");

        using (XmlDictionaryReader reader =
            JsonReaderWriterFactory.CreateJsonReader(buffer,
            new XmlDictionaryReaderQuotas()))
        {
            Read(reader);
        }


        //Message.InnerHtml="";
    }

    void Change(Object sender,EventArgs e)
    {

    Message.InnerHtml = "";

    for (int i=1;i<Table1.Rows.Count;i++)
        {
            Table1.Rows[i].Style.Add("display", "none");
            for (int j=0;j<Table1.Rows[i].Cells.Count;j++)
            {
                LiteralControl lc =(LiteralControl)Table1.Rows[i].Cells[j].Controls[0];
                //Message.InnerHtml+=lc.Text.IndexOf(tb1.Text)+"<br>";

                if (lc.Text.IndexOf(tb1.Text)!=-1)
                {   //Message.InnerHtml+="false";
                    Table1.Rows[i].Style["display"]="";
                }
            }
        }
    }

    void Button_Click(Object sender, EventArgs e)
    {

        TableRow thr = Table1.Rows[0];
        String ColumnNumber = Request.Form["__EVENTTARGET"].Replace("but", "");
        TableCell tch = thr.Cells[Int32.Parse(ColumnNumber)];
        tch.Controls.RemoveAt(1);

        //Message.InnerHtml="s="+sortOrd.Value+"!";
        if (fieldID.Value!=ColumnNumber)
        {
            sortOrd.Value="";
        }
        fieldID.Value = ColumnNumber;
        if ((sortOrd.Value=="")||(sortOrd.Value=="DESC"))
        {
            tch.Controls.Add(new LiteralControl("ASC"));
            sortOrd.Value = "ASC";
        }
        else
        {
            tch.Controls.Add(new LiteralControl("DESC"));
            sortOrd.Value = "DESC";
        }
        //Message.InnerHtml+=sortOrd.Value;

        if (sortOrd.Value=="ASC")
        {

            for (int i=Table1.Rows.Count-1;i>0;i--)
            {

                    TableRow tr = Table1.Rows[i];
                    TableCell tc = tr.Cells[Int32.Parse(ColumnNumber)];
                    LiteralControl lc =(LiteralControl)tc.Controls[0];
                    TableRow tr1=Table1.Rows[i-1];
                    TableCell tc1 = tr1.Cells[Int32.Parse(ColumnNumber)];
                    if (i>1)
                    {
                        LiteralControl lc1 =(LiteralControl)tc1.Controls[0];
                        if (String.Compare(lc1.Text,lc.Text)>0  )
                        {
                        Table1.Rows.Remove(tr1);
                        Table1.Rows.AddAt(i,tr1);

                        }
                    }

            }
        }

        if (sortOrd.Value=="DESC")
        {
            //Message.InnerHtml="";

            for (int i=Table1.Rows.Count-2;i>=1;i--)
            {
                    //Message.InnerHtml+="i="+i+",";
                    TableRow tr = Table1.Rows[i];
                    TableCell tc = tr.Cells[Int32.Parse(ColumnNumber)];
                    LiteralControl lc =(LiteralControl)tc.Controls[0];
                    TableRow tr1=Table1.Rows[i+1];
                    TableCell tc1 = tr1.Cells[Int32.Parse(ColumnNumber)];
                    if (i>0)
                    {
                        LiteralControl lc1 =(LiteralControl)tc1.Controls[0];
                        //Message.InnerHtml +=lc1.Text+","+lc.Text+"<br>";
                        if (String.Compare(lc1.Text,lc.Text)>0  )
                        {
                        Table1.Rows.Remove(tr1);
                        Table1.Rows.AddAt(i,tr1);
                        //Message.InnerHtml+="move<br>";
                        }
                    }

            }
        }

    }

</script>

<head>
    <title>Untitled</title>
</head>

<body runat="server">



<form id="form1" runat="server">
    <span id="Message" runat="server"/>
    <input type="hidden" name="sortOrd" id="sortOrd" value="" runat="server"/>
    <input type="hidden" name="fieldID" id = "fieldID" value="0" runat="server"/>
    <asp:TextBox id=tb1 runat="server" ontextchanged="Change" autopostback="true"/><br />
</form>
<asp:table borderwidth="2px" runat="server" id="Table1" >

</asp:Table>
</body>

</html>