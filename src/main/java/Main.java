import com.opencsv.CSVWriter;
import entity.StudentsEntity;
import jakarta.persistence.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try{
            transaction.begin();

            // list of string to save to CSV
            List<String[]> csvSave = new ArrayList<>();
            String[] header = {"id", "LastName", "FirstName"};
            csvSave.add(header);

            TypedQuery<StudentsEntity> query = entityManager.createNamedQuery("ShowAll", StudentsEntity.class);
            for(StudentsEntity s : query.getResultList()){
                System.out.println(s);
                // adding each row to list of strings
                csvSave.add(s.getStringTable());
            }

            // saving prepered list of strings to CSV
            try (CSVWriter writer = new CSVWriter(new FileWriter("test.csv"))) {
                writer.writeAll(csvSave);
            }

            // wypisanie avg ocen dla przedmiotu
            Query q = entityManager.createNativeQuery("select MAX(SubjectID) from Subjects");
            int ふ = (int) q.getSingleResult();

            Query q1 = entityManager.createNativeQuery("select name from Subjects where SubjectID =:id");
            Query q2 = entityManager.createNativeQuery("select AVG(points) from grade where SubjectID =:id");
            Query q3 = entityManager.createNativeQuery("select COUNT(SubjectID) from grade where SubjectID =:id");

            List<String[]> ばか = new ArrayList<>();
            String[] head = {"Subject: AVG: Count:"};
            ばか.add(head);
            for(int い = 1;い<=ふ ;い++){
                q2.setParameter("id",い);
                q3.setParameter("id",い);
                q1.setParameter("id",い);
                String[] s = {q1.getResultList().toString(),q2.getResultList().toString(),q3.getResultList().toString()};
                ばか.add(s);
            }

            for( String[] あ : ばか){
                for(String お : あ) {
                    System.out.print(お);
                }
                System.out.println();
            }

            // Add
            StudentsEntity student = new StudentsEntity();
            student.setFirstName("Misia");
            student.setLastName("Pysia");
            entityManager.persist(student);

            // Find
            StudentsEntity st = entityManager.find(StudentsEntity.class, 1);
            System.out.println(st);

            // Update object is updated when the entity is commited
            st.setFirstName("Mielonka");
            System.out.println(st);

            // Remove
            entityManager.remove(student);


            transaction.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }
    }

}

