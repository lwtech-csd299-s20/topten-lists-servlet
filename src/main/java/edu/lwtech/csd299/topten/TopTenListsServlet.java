package edu.lwtech.csd299.topten;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.log4j.*;
import freemarker.core.*;
import freemarker.template.*;

@WebServlet(name = "TopTenListsServlet", urlPatterns = {"/lists"}, loadOnStartup = 0)
public class TopTenListsServlet extends HttpServlet {

    private static final long serialVersionUID = 2020111122223333L;
    private static final Logger logger = Logger.getLogger(TopTenListsServlet.class);

    private static final String TEMPLATE_DIR = "/WEB-INF/classes/templates";
    private static final Configuration freemarker = new Configuration(Configuration.getVersion());

    private static DAO<Member> membersDao = null;
    private static DAO<TopTenList> listsDao = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.warn("=========================================");
        logger.warn("  TopTenListsServlet init() started");
        logger.warn("    http://localhost:8080/topten/lists");
        logger.warn("=========================================");

        logger.info("Getting real path for templateDir");
        String templateDir = config.getServletContext().getRealPath(TEMPLATE_DIR);
        logger.info("...real path is: " + templateDir);
        
        logger.info("Initializing Freemarker. templateDir = " + templateDir);
        try {
            freemarker.setDirectoryForTemplateLoading(new File(templateDir));
        } catch (IOException e) {
            logger.error("Template directory not found in directory: " + templateDir, e);
        }
        logger.info("Successfully Loaded Freemarker");
        
        membersDao = new MemberMemoryDAO();
        addDemoMemberData();
        listsDao = new TopTenListMemoryDAO();
        addDemoTopTenListData();

        logger.warn("Initialize complete!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("IN - GET " + request.getRequestURI());
        long startTime = System.currentTimeMillis();

        String command = request.getParameter("cmd");
        if (command == null) command = "home";


        //TODO: Get owner from session
        int owner = 1;

        String template = "";
        Map<String, Object> model = new HashMap<>();
        model.put("owner", owner);

        //TODO: Add more URL commands to the servlet
        switch (command) {

            case "add":
                template = "add.tpl";
                break;

            case "home":
                template = "home.tpl";
                break;

            case "show":
                String indexParam = request.getParameter("index");
                int index = (indexParam == null) ? 0 : Integer.parseInt(indexParam);
                int numItems = listsDao.getAllIDs().size();
                int nextIndex = (index + 1) % numItems;
                int prevIndex = index - 1;
                if (prevIndex < 0) prevIndex = numItems-1;

                template = "show.tpl";
                model.put("topTenList", listsDao.getByIndex(index));
                model.put("listNumber", index+1);                   // Java uses 0-based indexes.  Users want to see 1-based indexes.
                model.put("prevIndex", prevIndex);
                model.put("nextIndex", nextIndex);
                break;
                
            default:
                logger.debug("Unknown GET command received: " + command);

                // Send 404 error response
                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e)  {
                    logger.error("IO Error: ", e);
                }
                return;
        }
        processTemplate(response, template, model);

        long time = System.currentTimeMillis() - startTime;
        logger.info("OUT- GET " + request.getRequestURI() + " " + time + "ms");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("IN -POST " + request.getRequestURI());
        long startTime = System.currentTimeMillis();
        
        String command = request.getParameter("cmd");
        if (command == null) command = "";

        //TODO: Get owner from session
        int owner = 1;

        String message = "";
        String template = "confirm.tpl";
        Map<String, Object> model = new HashMap<>();
        
        switch (command) {

            case "create":
                TopTenList newList = getTopTenListFromRequest(request, owner);

                if (newList == null) {
                    logger.info("Create request ignored because one or more fields were empty.");
                    message = "Your new TopTenList was not created because one or more fields were empty.";
                } else {
                    if (listsDao.insert(newList) > 0)
                        message = "Your new TopTen List has been created successfully.";
                    else
                        message = "There was a problem adding your list to the database.";
                }
                break;
                
            default:
                logger.info("Unknown POST command received: " + command);

                // Send 404 error response
                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e)  {
                    logger.error("IO Error: ", e);
                }
                return;
        }

        model.put("message", message);
       
        processTemplate(response, template, model);

        long time = System.currentTimeMillis() - startTime;
        logger.info("OUT- GET " + request.getRequestURI() + " " + time + "ms");
    }
    
    @Override
    public void destroy() {
        logger.warn("-----------------------------------------");
        logger.warn("  TopTenListsServlet destroy() completed");
        logger.warn("-----------------------------------------");
    }

    @Override
    public String getServletInfo() {
        return "topten-lists-servlet Servlet";
    }

    // ========================================================================

    private void processTemplate(HttpServletResponse response, String template, Map<String, Object> model) {
        logger.debug("Processing Template: " + template);
        
        try (PrintWriter out = response.getWriter()) {
            Template view = freemarker.getTemplate(template);
            view.process(model, out);
        } catch (TemplateException | MalformedTemplateNameException | ParseException e) {
            logger.error("Template Error: ", e);
        } catch (IOException e) {
            logger.error("IO Error: ", e);
        } 
    }

    private TopTenList getTopTenListFromRequest(HttpServletRequest request, int owner) {

        String description = request.getParameter("description");
        if (description == null) return null;

        List<String> items = new ArrayList<>();
        for (int i=10; i >= 1; i--) {
            String item = request.getParameter("item" + i);
            if (item == null || item == "")
                return null;
            items.add(item);
        }
        
        TopTenList newList = new TopTenList(description, items, owner);
        return newList;
    }

    // ======================================================================

    private void addDemoMemberData() {
        logger.debug("Creating demo Members...");

        Member member;
        member = new Member("fred@lwtech.edu", "12345678");
        membersDao.insert(member);
        member = new Member("tom@lwtech.edu", "12345678");
        membersDao.insert(member);
        member = new Member("mary@lwtech.edu", "12345678");
        membersDao.insert(member);

        logger.info(membersDao.size() + " members inserted");
    }

    private void addDemoTopTenListData() {
        logger.debug("Creating demo TopTenLists...");

        String description;
        List<String> items;
        int owner;
    
        owner = 1;

        description = "Top 10 Favorite Roman Numerals";
        items = Arrays.asList(
            "X",
            "IX",
            "VIII",
            "VII",
            "VI",
            "V",
            "IV",
            "III",
            "II",
            "I"
        );
        listsDao.insert(new TopTenList(description, items, owner));

        description = "Top 10 Favorite Planets";
        items = Arrays.asList(
            "Hollywood",
            "Neptune",
            "Uranus",
            "Venus",
            "Mercury",
            "Earth",
            "Mars",
            "Jupiter",
            "Saturn",
            "Pluto!!!"
        );
        listsDao.insert(new TopTenList(description, items, owner));
        
        description = "Top 10 Favorite Star Wars Movies";
        items = Arrays.asList(
            "III: Revenge of the Sith",
            "II: Attack of the Clones",
            "VIII: The Last Jedi",
            "IX: The Rise of Skywalker",
            "VI: Return of the Jedi",
            "I: The Phantom Menace",
            "VII: The Force Awakens",
            "The Mandelorian Compilation",
            "IV: A New Hope",
            "V: The Empire Strikes Back"
        );
        listsDao.insert(new TopTenList(description, items, owner));
        
        logger.info(listsDao.size() + " lists inserted");
    }

}
