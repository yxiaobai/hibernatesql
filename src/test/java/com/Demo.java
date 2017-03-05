package com;


import com.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Demo {
    private Configuration cfg;
    private SessionFactory sf;
    private Session session;
    @Before
    public void init(){
        cfg = new Configuration().configure();
        sf = cfg.buildSessionFactory();
        session = sf.openSession();
        session.beginTransaction();
    }

    @Test
    public void insert(){
        Book b1 = new Book(0,"php项目开发",new BigDecimal(80));
        Book b2 = new Book(0,"redis数据库技术",new BigDecimal(95));
        Book b3 = new Book(0,"css3实战",new BigDecimal(30));
        Book b4 = new Book(0,"linux入门",new BigDecimal(20));
        session.save(b1);
        session.save(b2);
        session.save(b3);
        session.save(b4);
    }

    @Test
    public void query(){
        List<Book> books = session.createQuery("from Book").list();
        for (Book b : books){
            System.out.print(b.getId()+"--");
            System.out.println(b.getPrice());
        }
    }

    @Test
    public void work(){
        //jdbc api 接口
        session.doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                conn.createStatement().execute("create table db_t(t int)");
            }
        });
    }


    @Test
    public void update(){
        //Query query = session.createQuery("update Book set price=?,name=? where id=?");
        //query.setParameter(0,new BigDecimal(58.88));
        //query.setParameter(1,"jquery项目实战");
        //query.setParameter(2, 4);
        //int r = query.executeUpdate();

        //Query query = session.createQuery("update Book set price=price+5");
        //int r = query.executeUpdate();
        //System.out.println(r);

        //Book book = session.get(Book.class,6);
        //book.setName("redis技术");
        //book.setPrice(new BigDecimal(88));
        //session.update(book);


    }

    @Test
    public void ddl(){
        Query query = session.createQuery("delete from Book where name like :name");
        query.setParameter("name", "%开发");
        int result = query.executeUpdate();
        System.out.println(result);

        //session.delete(session.get(Book.class,7));

        //Query query = session.createQuery("delete Book where id=?");
        //query.setParameter(0, 8);
        //int result = query.executeUpdate();
        //System.out.println(result);
    }


    @Test
    public void show(){
        //List<Object> bn = session.createQuery("select name from Book").list();
        /*
        List<Object[]> bn = session.createQuery("select name,price from Book").list();
        for(Object[] objs : bn){
            System.out.println(objs[0]);
            System.out.println(objs[1]);
        }
        */

        //Query query = session.createQuery("select count(*) from Book");
        //int recordcount = Integer.parseInt(query.uniqueResult().toString());
        //System.out.println(recordcount);

        //Book ba  = session.load(Book.class, 30);
        //Book bb  = session.get(Book.class, 30);
        //System.out.println(ba);
        //System.out.println("-------------");
        //System.out.println(bb.getName());

        //System.out.println(ba==bb);

        //数据分页
        int pagesize = 3;
        int currpage = 2;
        int start = currpage*pagesize-pagesize;
        int recordcount = Integer.parseInt(session.createQuery("select count(*) from Book").uniqueResult().toString());
        int pagecount = recordcount%pagesize==0 ? recordcount/pagesize : recordcount/pagesize+1;
        Query query = session.createQuery("from Book order by price desc");
        query.setFirstResult(start);
        query.setMaxResults(pagesize);
        List<Book> books = query.list();
        for(Book b : books){
            System.out.println(b.getName());
        }
        System.out.println(recordcount);


    }

    @After
    public void close(){
        session.getTransaction().commit();
        session.close();
        sf.close();
    }
}
